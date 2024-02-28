import { Outlet } from "react-router-dom";
import { NavBar } from "./NavBar";
import events from "events";
import { WebSocketChargePointNotification } from "./conf/chargePointController";

// Define backend server port
const BACKEND_PORT = 8080

declare interface WebSocketListener {
    on(event: 'charge-point-update', listener: (message: WebSocketChargePointNotification) => void): this;
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
      let protocol = window.location.hostname.startsWith("localhost") || window.location.hostname.startsWith("127.0.0.1") ? "ws://" : "wss://"
      let websocketAddress = `${protocol}${window.location.hostname}:${BACKEND_PORT}/websocket/chargepoint`
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
          // Emit received message
          this.emit('charge-point-update', chargePointNotification)
          return
        }
        // TODO: Parsing other message
        console.log("Unknown received message from websocket : " + ev.data)
      }

      this.websocket.onclose = (ev: CloseEvent) => {
          console.log('Websocket disconnected from server');
          this.connected = false
          // If connection closed, try to reconnect the websocket 5 second later
          setTimeout(() => this.startWebSocket(), 5000)
      }
    }
}

export const wsManager = new WebSocketListener();
wsManager.startWebSocket()

export function Home() {
    return (
        <div className="App" style={{maxWidth: "true", height: "100vh", overflow: "hidden"}}>
          <NavBar/>
          <Outlet />
        </div>
    );
}


export default Home;

