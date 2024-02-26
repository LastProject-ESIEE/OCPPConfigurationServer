import React, { Dispatch, SetStateAction } from "react";
import { GlobalState } from "./GlobalState";
import { Grid, Input, Paper } from "@mui/material";

function DescriptionComponent(props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>
}) {

    return (
        <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3}}>
            <Grid direction={"column"} container justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Description : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Input
                        onChange={event => {
                            props.setGlobalState(prevState => {
                                return {
                                    configuration: prevState.configuration,
                                    firmware: prevState.firmware,
                                    description: event.target.value,
                                    name: prevState.name
                                }
                            })
                        }}
                        multiline minRows={4} maxRows={6} fullWidth={true}
                        placeholder="Description de la configuration"/>
                </Grid>
            </Grid>
        </Paper>
    )
}

export default DescriptionComponent;