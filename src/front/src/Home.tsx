import { Outlet } from "react-router-dom";
import { NavBar } from "./NavBar";

export function Home() {


    return (
        <div className="App">
            <NavBar />
            <Outlet />
        </div>
    );
}


export default Home;
