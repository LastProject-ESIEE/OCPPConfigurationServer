import React, { Dispatch, SetStateAction, useEffect, useState } from "react";
import { Grid, MenuItem, Paper, Select } from "@mui/material";
import { ErrorState, GlobalState } from "../../../conf/configurationController";

function FirmwareComponent(props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>,
    errorState: ErrorState
}) {
    //const [firmware, setFirmware] = useState(props.globalState.firmware);
    const [firmwareList, setFirmwareList] = useState<{ id: number, version: string }[]>([]);
    const backgroundColor = props.errorState.firmware === "" ? 'rgb(249, 246, 251)' : 'rgba(255, 0, 0, 0.2)'; // Replace with your desired colors

    useEffect(() => {
        const fetchFirmwareList = async () => {
            const response = await fetch('/api/firmware/all');
            const data = await response.json();
            setFirmwareList(data);
        };

        fetchFirmwareList();
    }, []);

    return (
        <Paper elevation={2} sx={{p: 2, mt: 3, backgroundColor}}>

            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Firmware : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Select
                        value={props.globalState.firmware}
                        onChange={event => {
                            //setFirmware(event.target.value as string)
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
                        {firmwareList && firmwareList.map((item) => {
                            let selected = false;
                            if(props.globalState.firmware == item.version){
                                selected=true
                            }
                            return (
                            <MenuItem key={"firmware-"+ item.id} value={item.version} selected={selected}>{item.version}</MenuItem>
                        )})}
                    </Select>
                </Grid>
                {props.errorState.firmware !== "" &&
                    <p>{props.errorState.firmware}</p>
                }
            </Grid>
        </Paper>
    )
}

export default FirmwareComponent;