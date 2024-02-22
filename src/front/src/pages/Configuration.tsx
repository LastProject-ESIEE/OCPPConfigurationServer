import { Box, Grid, List, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import { ConfigurationView, getConfigurationList } from "../conf/configurationController";
import { configure } from "@testing-library/react";
import { Link, RouteProps, RouterProps, useParams, useSearchParams } from "react-router-dom";


let confTest = [{id: 1, description: "Configuration 1"},
{id: 2, description: "Configuration 2"},
{id: 3, description: "Configuration 3"},
{id: 4, description: "Configuration 4"}]



export function ConfigurationListPage()  {
    const [items, setItems] = React.useState<ConfigurationView[]>([]);

    useEffect(() => {
        getConfigurationList().then((result: ConfigurationView[]) => setItems(result));
      }, [])

    return (
        <Box maxWidth={"true"}>
            <Grid container maxWidth={"true"} flexDirection={"row"} justifyContent={"center"}>
                <Grid container xs={2} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" justifyContent={"center"}>Nom</Typography>
                </Grid>
                <Grid container xs={10} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" justifyContent={"center"}>Decsription</Typography>
                </Grid>
            </Grid>
            <Box maxWidth={"true"}>
            <List style={{maxWidth: "true"}}>
                        {items.map(configuration => {
                            return (
                                <Link to={{ pathname: 'edit/' + configuration.id}} style={{ textDecoration: 'none' }} >
                                    <ListItemButton style={{maxWidth: "true"}} onClick={(ev) => console.log("Display " + configuration.id)}>
                                        <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                            <Grid container xs={2} maxWidth={"true"} justifyContent={"center"}>
                                                <Typography variant="body2">{configuration.name}</Typography>
                                            </Grid>
                                            <Grid container xs={10} maxWidth={"true"} justifyContent={"center"}>
                                                <Typography variant="body2">{configuration.description}</Typography>
                                            </Grid>
                                        </Grid>
                                    </ListItemButton>
                                </Link>
                            )
                        })}
                </List>
            </Box>
        </Box>
    )
}

export function ConfigurationEditPage() {
    // Get id from router parameter
    const { id } = useParams();

    return (
        <Box>
            <p>Edition of configuration {id}</p>
        </Box>
    );
}