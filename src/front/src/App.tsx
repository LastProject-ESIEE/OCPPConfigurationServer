import React, { useEffect } from 'react';
import './styles/App.css';
import { displayHomePage } from './pages/HomePage';
import FullWidthTabs from './pages/HomeMenu';


function Home() {
  
  useEffect(() =>
  {
    const ws = new WebSocket('ws://localhost:8080/myHandler');
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
