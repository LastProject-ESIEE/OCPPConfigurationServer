import { Avatar, Button, Grid, IconButton, Toolbar } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import { useState } from "react";

const buttons = [
    {
        roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
        label: "Configuration",
        href: "/configuration",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Afficher",
                href: "",
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Créer",
                href: "/new",
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Modifier",
                href: "/edit",
            },
        ]
    },
    {
        roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
        label: "Consulter les bornes",
        href: "/chargepoint",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Liste des bornes",
                href: "",
            }
        ]
    },
    {
        roles: ["ADMINISTRATOR", "EDITOR"],
        label: "Firmware",
        href: "/firmware",
        subButtons: [
            {
                roles: ["ADMINISTRATOR", "EDITOR", "VISUALIZER"],
                label: "Afficher/Modifier",
                href: "",
            },
            {
                roles: ["ADMINISTRATOR", "EDITOR"],
                label: "Créer",
                href: "/new",
            }
        ]
    },
    {
        roles: ["ADMINISTRATOR"],
        label: "Gestion des comptes",
        href: "/acounts",
        subButtons: [
            {
                roles: ["ADMINISTRATOR"],
                label: "Création",
                href: "/new",
            },
            {
                roles: ["ADMINISTRATOR"],
                label: "Modification",
                href: "/edit",
            }
        ]
    },
]

export function Home() {
    const [selectedButton, setSelectedButton] = useState("Configuration");

    return (
        <div className="App">
            <AppBar position="static">
                <Toolbar sx={{display: 'flex', justifyContent: 'space-between'}}>
                    {/* Two lines of buttons with spacing using MUI Grid */}
                    <Grid container spacing={2} direction="column" alignItems="center">
                        {/* First line of buttons */}
                        <Grid item container spacing={2} justifyContent="center">
                            {
                                buttons.filter(item => item.roles.includes("ADMINISTRATOR")).map(item =>
                                    <Grid item>
                                        <Button onClick={() => setSelectedButton(item.label)} variant="contained">{item.label}</Button>
                                    </Grid>)
                            }
                        </Grid>
                        {/* Second line of buttons */}
                        <Grid item container spacing={2} justifyContent="center">
                            {
                                buttons.filter(item => item.label === selectedButton).map(item =>
                                    item.subButtons.filter(subButton => subButton.roles.includes("ADMINISTRATOR")).map(subButton =>
                                        <Grid item>
                                            <Button variant="contained">{subButton.label}</Button>
                                        </Grid>)
                                )

                            }
                        </Grid>
                    </Grid>

                    {/* Profile picture on the right */}
                    <IconButton size="large" aria-label="profile picture">
                        <Avatar src="profile-picture.png" alt="Profile Picture"/>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </div>
    );
}

export default Home;
