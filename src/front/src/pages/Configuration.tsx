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
        <Box maxWidth={"true"} height={"90vh"} maxHeight={"90vh"}>
            <Grid key="grid-header" container maxWidth={"true"} flexDirection={"row"} justifyContent={"center"} paddingRight={2}>
                <Grid key="grid-header-item-1" item xs={3} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Nom</Typography>
                </Grid>
                <Grid key="grid-header-item-2" item xs={6} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Description</Typography>
                </Grid>
                <Grid key="grid-header-item-3" item xs={3} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" align="center">Dernière modification</Typography>
                </Grid>
            </Grid>
            <Box key="box-list-container" maxWidth={"true"}  maxHeight={"true"}>
                <List key="configuration-list" style={{maxWidth: "true", maxHeight:'50vh', overflow: 'auto'}}>
                        {items.map(configuration => {
                            return (
                               <Box key={"box-configuration-edit-path-" + configuration.id}  paddingTop={1}>
                                    <Link key={"configuration-edit-path-" + configuration.id}  to={{ pathname: 'edit/' + configuration.id}} style={{ textDecoration: 'none', paddingTop: 10 }}>
                                        <ListItemButton style={{maxWidth: "true", padding: 0, paddingTop: 3, borderRadius: 100, color: 'black', backgroundColor: '#E1E1E1'}}>
                                            <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                                                <Grid item xs={3} maxWidth={"true"} justifyContent={"center"}>
                                                    <Typography variant="body1" align="center">{configuration.name}</Typography>
                                                </Grid>
                                                <Grid item xs={6} maxWidth={"true"} justifyContent={"center"}>
                                                    <Typography variant="body1" align="center">{configuration.description === "" ? "Pas de description" : configuration.description}</Typography>
                                                </Grid>
                                                <Grid item xs={3} maxWidth={"true"} justifyContent={"center"}>
                                                    <Typography variant="body1" align="center">{new Date(configuration.timestamp).toLocaleString()}</Typography>
                                                </Grid>
                                            </Grid>
                                        </ListItemButton>
                                    </Link>
                               </Box>
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
            <p>Édition de la configuration {id}</p>
        </Box>
    );
}