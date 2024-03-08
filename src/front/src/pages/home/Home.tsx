import { Outlet } from "react-router-dom";
import { NavBar } from "../../sharedComponents/NavBar";
import events from "events";
import { WebSocketChargePointNotification } from "../../conf/chargePointController";
import { Fab, IconButton } from "@mui/material";
import EmailIcon from '@mui/icons-material/Email';
import DisplayNotification, { NotificationType, NotificationMessage } from "../../sharedComponents/DisplayNotification";
import { useState } from "react";
import { Box } from "@mui/system";
// Define backend server port
const BACKEND_PORT = 8080

declare interface WebSocketListener {
    on(event: 'charge-point-update', listener: (message: WebSocketChargePointNotification) => void): this;
    on(event: 'notify', listener: (message: NotificationMessage) => void): this;
}

class WebSocketListener extends events.EventEmitter {
    connected: boolean;
    websocket: WebSocket | undefined;

    constructor(){
      super()
      this.connected = false;
    }

    // Start 
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
        const chargePointNotification = JSON.parse(ev.data) as WebSocketChargePointNotification
        if(chargePointNotification){
          this.emit('charge-point-update', chargePointNotification)
          return
        }
        const notifMessage = JSON.parse(ev.data) as NotificationMessage
        if(notifMessage){
          this.emitNotification(notifMessage)
          return
        }
        console.log("Unknown received message from websocket : " + ev.data)
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
              style={{position: "fixed", top: "92%", right: "3%"}}
              onClick={() => {
                setOpenNotification(true)
              }}>
              <EmailIcon 
                    color="primary"
                    fontSize={"large"}
                >
              </EmailIcon>
            </IconButton>

          )}
          <NavBar/>
          <Outlet />
          <DisplayNotification open={openNotification} onClose={() => setOpenNotification(false)}/>
        </div>
    );
}


export default Home;

