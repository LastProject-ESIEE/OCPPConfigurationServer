import React, { Dispatch, SetStateAction } from "react";
import { Grid, Input, Paper } from "@mui/material";
import { ErrorState } from "../../../../conf/configurationController";

export default function DescriptionComponent(props: {
    value: string;
    setValue: Dispatch<SetStateAction<string>>,
    errorState: ErrorState
}) {
    const backgroundColor = props.errorState.description === "" ? 'rgb(249, 246, 251)' : 'rgba(255, 0, 0, 0.2)'; // Replace with your desired colors

    return (
        <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3, backgroundColor}}>
            <Grid direction={"column"} container justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Description : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Input
                        onChange={event => {
                            props.setValue(event.target.value)
                        }}
                        value={props.value}
                        multiline minRows={4} maxRows={6} fullWidth={true}
                        placeholder="Description de la configuration"/>
                </Grid>
                {props.errorState.description !== "" &&
                    <p>{props.errorState.description}</p>
                }
            </Grid>
        </Paper>
    )
}
