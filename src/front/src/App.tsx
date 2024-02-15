import React from 'react';
import './styles/App.css';
import FullWidthTabs from './pages/HomeMenu';

function App(): JSX.Element {
    return (
        <div>
            {FullWidthTabs()}
        </div>
    );
}

export default App;
