import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import { GlobalState } from "./GlobalState";
import { Grid, MenuItem, Paper, Select } from "@mui/material";

function FirmwareComponent(props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>
}) {
    const [firmware, setFirmware] = useState("");
    const [firmwareList, setFirmwareList] = useState<{ id: number, version: string }[]>([]);

    useEffect(() => {
        const fetchFirmwareList = async () => {
            const response = await fetch('/api/firmware/all');
            const data = await response.json();
            setFirmwareList(data);
        };

        fetchFirmwareList();
    }, []);


    return (
        <Paper elevation={2} sx={{p: 2, mt: 3}}>

            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Firmware : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Select
                        value={firmware}
                        onChange={event => {
                            setFirmware(event.target.value as string)
                            props.setGlobalState(prevState => {
                                return {
                                    configuration: prevState.configuration,
                                    firmware: event.target.value as string,
                                    description: prevState.description,
                                    name: prevState.name
                                }
                            })
                        }}
                        fullWidth={true}>

                        {firmwareList && firmwareList.map((item) => (
                            <MenuItem key={item.id} value={item.id} selected={true}>{item.version}</MenuItem>
                        ))}
                    </Select>
                </Grid>
            </Grid>
        </Paper>
    )
}

export default FirmwareComponent;