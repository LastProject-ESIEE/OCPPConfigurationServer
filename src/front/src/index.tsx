import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.css';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import About from "./pages/About";
import Error from "./pages/Error";
import CreateConfig from "./pages/configuration/create/CreateConfig";
import Home from "./Home";
import { ChargePointTable } from './pages/BornesTable';
import Account from "./pages/Account";
import { UserTable } from "./pages/UserTable";
import { TechnicalLogTable } from "./pages/TechnicalLogsTable";
import FirmwareTable from "./pages/firmware/FirmwareTable";
import { BusinessLogTable } from "./pages/BusinessLogTable";
import ConfigurationTable, { ConfigurationEditPage} from "./pages/configuration/display/ConfigurationTable";

const root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);
root.render(
    <React.StrictMode>
        <Router>
            <Routes>
                {/*TODO: remplacer par les vrais components dans les routes*/}
                <Route path="" element={<Login/>}/>
                <Route path="about" element={<About/>}/>
                <Route path="home" element={<Home/>}>
                    <Route path="" element={<Navigate to="chargepoint"/>}/>
                    <Route path="configuration">
                        <Route path="" element={<ConfigurationTable/>}/>
                        <Route path="edit/:id" element={<ConfigurationEditPage/>}/>
                        <Route path="new" element={<CreateConfig/>}/>
                    </Route>
                    <Route path="chargepoint">
                        <Route path="" element={<ChargePointTable/>}/>
                        <Route path="display/:id" element={<Error/>}/>
                    </Route>
                    <Route path="firmware">
                        <Route path="" element={<FirmwareTable/>}/>
                        <Route path="new" element={<Error/>}/>
                    </Route>
                    <Route path="account">
                        <Route path="" element={<UserTable/>}/>
                        <Route path="new" element={<Error/>}/>
                    </Route>
                    <Route path="logs">
                        <Route path="" element={<Navigate to="business"/>}/>
                        <Route path="business" element={<BusinessLogTable/>}/>
                        <Route path="technical" element={<TechnicalLogTable/>}/>
                    </Route>
                    <Route path="myAccount" element={<Account/>}/>
                </Route>
                <Route path="*" element={<Error/>}/>
                {/* Add more routes as needed */}
            </Routes>
        </Router>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
