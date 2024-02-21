import { Box, Grid, List, ListItemButton, Typography } from "@mui/material";


let confTest = [{id: 1, description: "Configuration 1"},
{id: 2, description: "Configuration 2"},
{id: 3, description: "Configuration 3"},
{id: 4, description: "Configuration 4"}]



export function ConfigurationListPage()  {

    return (
        <Box maxWidth={"true"}>
            <Grid container maxWidth={"true"} flexDirection={"row"} justifyContent={"center"}>
                <Grid container xs={2} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" justifyContent={"center"}>Id Config</Typography>
                </Grid>
                <Grid container xs={10} maxWidth={"true"} justifyContent={"center"}>
                    <Typography maxWidth={"true"} variant="h6" justifyContent={"center"}>Decsription</Typography>
                </Grid>
            </Grid>
                <List style={{maxWidth: "true"}}>
                    <Grid container maxWidth={"true"} >
                    {confTest.map(conf => {
                        return (
                            <ListItemButton style={{maxWidth: "true"}}>
                                <Grid container maxWidth={"true"} >
                                    <Grid container  xs={2} maxWidth={"true"} >
                                        <Typography variant="body2">{conf.id}</Typography>
                                    </Grid>
                                    <Grid container xs={12} maxWidth={"true"}>
                                        <Typography variant="body2">{conf.description}</Typography>
                                    </Grid>
                                </Grid>
                            </ListItemButton>
                        )
                    })}
                    </Grid>
                </List>
        </Box>
    )
}