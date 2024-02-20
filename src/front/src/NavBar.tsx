import { Link, useLocation } from "react-router-dom";
import { useEffect, useState } from "react";
import { ButtonData, buttons } from "./conf/homeDefinition";
import { Button, Grid, Toolbar } from "@mui/material";
import AppBar from "@mui/material/AppBar";
import Typography from "@mui/material/Typography";

function ButtonLink(props: { label: string, href: string, disabled?: boolean }): JSX.Element {
    return (
        <Grid item key={props.label}>
            <Link to={props.href}><Button disabled={props.disabled}
                                          variant="contained">{props.label}</Button></Link>
        </Grid>
    )
}

export function NavBar() {
    const location = useLocation();
    const [currentButton, setCurrentButton] = useState<ButtonData | null>(null);
    const userRole = "ADMINISTRATOR";

    // Update the currentButton state when the URL changes
    useEffect(() => {
        const matchingButton = buttons.find(item => {
            const buttonPath = `/home${item.href}`;
            return location.pathname.startsWith(buttonPath);
        });
        setCurrentButton(matchingButton || null);
    }, [location.pathname]);

    return (
        <AppBar position="static">
            <Toolbar>
                <Grid container alignItems="center" justifyContent="space-between">
                    {/* Buttons on the left */}
                    <Grid item>
                        <Grid container direction="column" spacing={2}>
                            {/* First line of buttons */}
                            <Grid item container direction="row" spacing={2} justifyContent="center">
                                {buttons.filter((item) => item.roles.includes(userRole))
                                    .map((item) => {
                                            let disabled = false;
                                            if (item === currentButton) {
                                                disabled = true;
                                            }
                                            return <ButtonLink disabled={disabled} label={item.label}
                                                               href={`/home${item.href}`}/>
                                        }
                                    )}
                            </Grid>
                            {/* Second line of buttons */}
                            <Grid item container spacing={2} justifyContent="center">
                                {currentButton &&
                                    currentButton.subButtons
                                        .filter((subButton) => subButton.roles.includes(userRole))
                                        .map((subButton) => {
                                            let disabled = false;
                                            if (location.pathname === `/home${currentButton.href}${subButton.href}`) {
                                                disabled = true
                                            }
                                            return <ButtonLink disabled={disabled} label={subButton.label}
                                                               href={`/home${currentButton.href}${subButton.href}`}/>
                                        })}
                            </Grid>
                        </Grid>
                    </Grid>

                    {/* Profile box on the right */}
                    <Grid item>
                        <Link style={{textDecoration: 'none', color: 'inherit'}} to="/home/myAccount">

                            <Grid container direction="row" alignItems="center">
                                <Grid item>
                                    <Grid container direction="column" alignItems="center">
                                        <Typography variant="body1">Julien</Typography>
                                        <Typography variant="body2">Administrator</Typography>
                                    </Grid>
                                </Grid>
                            </Grid>
                        </Link>

                    </Grid>
                </Grid>
            </Toolbar>
        </AppBar>
    )
}