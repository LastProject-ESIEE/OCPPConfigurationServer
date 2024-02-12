import React from "react";
import logo from "../assets/logo-brs.png";

export function displayHomePage(): React.ReactNode {
    return (
        <div className="screen">
            <div className="bandeau">

                <div className="logo">
                    {/* Contenu de la zone "Logo" */}
                    <img src={logo} alt="BRS Logo" className="brs-logo" />
                </div>

                <div className="action">
                    {/* Contenu de la zone "Action" */}
                    <p>Action Content</p>
                </div>

                <div className="profile">
                    {/* Contenu de la zone "Profile" */}
                    <p>Profil Content</p>
                </div>
            </div>
            {/*action à display*/}
            <p>infos de la borne à afficher </p>
        </div>
    );
}