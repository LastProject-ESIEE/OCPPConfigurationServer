import { Box, Grid, List, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import { ConfigurationView, getConfigurationList } from "../conf/configurationController";
import { Link, useParams } from "react-router-dom";


export function ConfigurationListPage()  {
    const [items, setItems] = React.useState<ConfigurationView[]>([]);

    useEffect(() => {
        getConfigurationList().then((result: ConfigurationView[]) => setItems(result));
      }, [])

    return (
        <Box maxWidth={"true"}>
            <Grid key="grid-header" container maxWidth={"true"} flexDirection={"row"} justifyContent={"center"}>
                <Grid key="grid-header-item-1" item xs={3} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Name</Typography>
                </Grid>
                <Grid key="grid-header-item-2" item xs={6} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Decsription</Typography>
                </Grid>
                <Grid key="grid-header-item-3" item xs={3} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Last update</Typography>
                </Grid>
            </Grid>
            <Box key="box-list-container" maxWidth={"true"}>
                <List key="configuration-list" style={{maxWidth: "true",height: 700, maxHeight: 700, overflow: 'auto'}}>
                        {items.map(configuration => {
                            return (
                                <Link key={"configuration-edit-path-" + configuration.id}  to={{ pathname: 'edit/' + configuration.id}} style={{ textDecoration: 'none' }} >
                                    <ListItemButton style={{maxWidth: "true", padding: 0, paddingTop: 4}}>
                                        <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                            <Grid item xs={3} maxWidth={"true"} justifyContent={"center"}>
                                                <Typography variant="body1" align="center">{configuration.name}</Typography>
                                            </Grid>
                                            <Grid item xs={6} maxWidth={"true"} justifyContent={"center"}>
                                                <Typography variant="body1" align="center">{configuration.description === "" ? "No description" : configuration.description}</Typography>
                                            </Grid>
                                            <Grid item xs={3} maxWidth={"true"} justifyContent={"center"}>
                                                <Typography variant="body1" align="center">{new Date(configuration.timestamp).toLocaleString()}</Typography>
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