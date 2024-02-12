import React from 'react';
import './styles/App.css';
import { displayHomePage } from './pages/HomePage';
import FullWidthTabs from './pages/HomeMenu';

function App(): JSX.Element {
  return (
    <div>
      {FullWidthTabs()}
    </div>
  );
}

export default App;
