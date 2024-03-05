import React from 'react';
import { BrowserRouter as Router, Navigate, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import About from "./pages/about/About";
import Home from "./pages/home/Home";
import ConfigurationTable from "./pages/home/configuration/ConfigurationTable";
import ConfigurationEditPage from "./pages/home/configuration/ConfigurationEditPage";
import CreateConfig from "./pages/home/configuration/CreateConfig";
import ChargePointTable from "./pages/home/chargepoint/ChargePointTable";
import CreateChargepoint from "./pages/home/chargepoint/CreateChargepoint";
import Error from "./pages/other/Error";
import FirmwareTable from "./pages/home/firmware/FirmwareTable";
import UserTable from "./pages/home/account/UserTable";
import BusinessLogTable from "./pages/home/logs/BusinessLogTable";
import TechnicalLogTable from "./pages/home/logs/TechnicalLogTable";
import Account from "./pages/home/account/Account";
import CreateFirmware from './pages/home/firmware/CreateFirmware';


function App(): JSX.Element {
    return (
        <React.StrictMode>
            <Router>
                <Routes>
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
                            <Route path="new" element={<CreateChargepoint/>}/>
                            <Route path="display/:id" element={<Error/>}/>
                        </Route>
                        <Route path="firmware">
                            <Route path="" element={<FirmwareTable/>}/>
                            <Route path="new" element={<CreateFirmware/>}/>
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
                </Routes>
            </Router>
        </React.StrictMode>
    );
}

export default App;
