import { Avatar, Button, Grid, IconButton, Toolbar } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { ButtonData, buttons } from "./conf/homeDefinition";

export function Home() {
    const location = useLocation();
    const [currentButton, setCurrentButton] = useState<ButtonData | null>(null);

    // Update the currentButton state when the URL changes
    useEffect(() => {
        const matchingButton = buttons.find(item => {
            const buttonPath = `/home${item.href}`;
            return location.pathname.startsWith(buttonPath);
        });
        setCurrentButton(matchingButton || null);
    }, [location.pathname]);

    return (
        <div className="App">
            <AppBar position="static">
                <Toolbar sx={{display: 'flex', justifyContent: 'space-between'}}>
                    {/* Two lines of buttons with spacing using MUI Grid */}
                    <Grid container spacing={2} direction="column" alignItems="center">
                        {/* First line of buttons */}
                        <Grid item container spacing={2} justifyContent="center">
                            {buttons.filter(item => item.roles.includes("ADMINISTRATOR")).map(item =>
                                <Grid item key={item.label}>
                                    <Link to={`/home${item.href}`}><Button variant="contained">{item.label}</Button></Link>
                                </Grid>
                            )}
                        </Grid>
                        {/* Second line of buttons */}
                        <Grid item container spacing={2} justifyContent="center">
                            {currentButton && currentButton.subButtons.filter(subButton => subButton.roles.includes("ADMINISTRATOR")).map(subButton =>
                                <Grid item key={subButton.label}>
                                    <Link to={`/home${currentButton.href}${subButton.href}`}><Button variant="contained">{subButton.label}</Button></Link>
                                </Grid>
                            )}
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
