import React, { Dispatch, SetStateAction } from "react";
import { GlobalState } from "./GlobalState";
import { Grid, Input, Paper } from "@mui/material";

function TitleComponent(props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>
}) {
    return (
        <Paper elevation={2} sx={{p: 2, mt: 3}}>
            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Titre de la configuration : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Input
                        onChange={event => {
                            props.setGlobalState(prevState => {
                                return {
                                    configuration: prevState.configuration,
                                    firmware: prevState.firmware,
                                    description: prevState.description,
                                    name: event.target.value
                                }
                            })
                        }}
                        fullWidth={true}
                        placeholder="Titre"/>
                </Grid>
            </Grid>
        </Paper>
    )
}

export default TitleComponent;