import React, { useEffect } from 'react';
import './styles/App.css';
import FullWidthTabs from './pages/HomeMenu';

function Home() {

    useEffect(() =>
    {
        //const ws = new WebSocket('ws://localhost:8080/websocket/chargepoint');
        const ws = new WebSocket('ws://app-91d7e400-ac56-44dd-81e5-c2f07e397127.cleverapps.io:8080/websocket/chargepoint');

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
