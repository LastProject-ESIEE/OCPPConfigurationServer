import React, { useEffect } from 'react';
import './styles/App.css';
import FullWidthTabs from './pages/HomeMenu';

function Home() {
    useEffect(() =>
    {
        // Get current server address
        let address = window.location.href.replace("http://", "").replace("https://", "").replace("/","")
        let protocol = address.startsWith("localhost") || address.startsWith("127.0.0.1") ? "ws://" : "wss://"
        let websocketAddress = protocol + address + "/websocket/chargepoint"
        console.log("Connecting to the websocket address: " + websocketAddress)
        const ws = new WebSocket(websocketAddress);

        if(ws != null){
            ws.onopen = (ev: Event) => {
                console.log('Connected to server');
                ws.send('Hello, server!');
            }

            ws.onmessage = (ev: MessageEvent<any>) => {
                var message = ev.data
                console.log(`Received message from server: ${message}`);
            }

            ws.onclose = (ev: CloseEvent) => {
                console.log('Disconnected from server');
            }
        }
    }, [])

    return (
        <div>Home</div>
    )
}

function App(): JSX.Element {
    return (
        <div>
            {Home()}
            {FullWidthTabs()}
        </div>
    );
}

export default App;
