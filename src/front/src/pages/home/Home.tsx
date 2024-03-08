import { Outlet } from "react-router-dom";
import { NavBar } from "../../sharedComponents/NavBar";
import events from "events";
import { WebSocketChargePointNotification } from "../../conf/chargePointController";
import { IconButton } from "@mui/material";
import DisplayNotification, { NotificationType, NotificationMessage } from "../../sharedComponents/DisplayNotification";
import { useState } from "react";
import NotificationImportantIcon from '@mui/icons-material/NotificationImportant';

// Define backend server port
const BACKEND_PORT = 8080

declare interface WebSocketListener {
    on(event: 'charge-point-update', listener: (message: WebSocketChargePointNotification) => void): this;
    on(event: 'notify', listener: (message: NotificationMessage) => void): this;
}

type WebSocketMessage = {
  name: "ChargePointWebsocketNotification" | "CriticalityWebsocketNotification",
  value: string,
}

class WebSocketListener extends events.EventEmitter {
    connected: boolean;
    websocket: WebSocket | undefined;

    constructor(){
      super()
      this.connected = false;
    }

    startWebSocket(): void {
      if(this.connected){
        console.error("WebSocket connection is already established.")
        return
      }
      let isLocal = window.location.hostname.startsWith("localhost") || window.location.hostname.startsWith("127.0.0.1")
      let protocol = isLocal ? "ws://" : "wss://"
      let websocketAddress = `${protocol}${window.location.hostname}${isLocal ? ":" + BACKEND_PORT : ""}/websocket/chargepoint`
      console.log("Connecting to the websocket address: " + websocketAddress)
      this.websocket = new WebSocket(websocketAddress);
      
      this.websocket.onopen = (ev: Event) => {
          console.log('Websocket connected to the server');
          this.connected = true
      }

      this.websocket.onmessage = (ev: MessageEvent<any>) => {
        // Try parse as WebSocketChargePointNotification
        const message = JSON.parse(ev.data) as WebSocketMessage
        switch(message.name){
          case "ChargePointWebsocketNotification":
            let wsChargePointNotification = JSON.parse(message.value) as WebSocketChargePointNotification
            this.emit('charge-point-update', wsChargePointNotification)
            break;
          case "CriticalityWebsocketNotification":
            let wsNotification = JSON.parse(message.value) as NotificationMessage
            this.emitNotification(wsNotification)
            break;
          default:
            console.warn("Unknown received message from websocket : " + ev.data)
        }
      }

      this.websocket.onclose = (ev: CloseEvent) => {
          console.log('Websocket disconnected from server');
          this.connected = false
          // If connection closed, try to reconnect the websocket 5 second later
          setTimeout(() => this.startWebSocket(), 5000)
      }
    }

    emitNotification(message: NotificationMessage){
      this.emit("notify", message)
    }
}

export const wsManager = new WebSocketListener();
wsManager.startWebSocket()

export function Home() {
    const [openNotification, setOpenNotification] = useState(false);
    return (
        <div className="App" style={{maxWidth: "true", height: "100vh", overflow: "hidden"}}>
          {!openNotification && (
            <IconButton                     
              style={{position: "fixed", top: "92%", right: "3%", zIndex: 999}}
              onClick={() => {
                setOpenNotification(true)
              }}>
              <NotificationImportantIcon 
                    color="primary"
                    fontSize={"large"}
                >
              </NotificationImportantIcon>
            </IconButton>
          )}
          <NavBar/>
          <Outlet />
          <DisplayNotification open={openNotification} onClose={() => setOpenNotification(false)}/>
        </div>
    );
}


export default Home;

