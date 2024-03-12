import {useState} from "react";
import { Grid, MenuItem, Paper, Select } from "@mui/material";
import { ErrorState } from "../../../../conf/configurationController";
import { Firmware } from "../../../../conf/FirmwareController";

export default function FirmwareComponent(props: {
    current?: Firmware,
    firmwareList: Firmware[],
    onSelectionChange: (firmware: Firmware) => void,
    errorState: ErrorState,
}) {
    const [selectedItem, setSelectedItem] = useState<number | undefined>(props.current?.id);
    const backgroundColor = props.errorState.firmware === "" ? 'rgb(249, 246, 251)' : 'rgba(255, 0, 0, 0.2)'; // Replace with your desired colors

    return (
        <Paper elevation={2} sx={{p: 2, mt: 3, backgroundColor}}>
            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Firmware : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Select
                        value={selectedItem ?? ""}
                        onChange={event => {
                            let selection = event.target.value as number
                            if (selection) {
                                let selectedFirmware = props.firmwareList.find(element => element.id === selection)
                                if (selectedFirmware) {
                                    props.onSelectionChange(selectedFirmware)
                                    setSelectedItem(selection)
                                }
                            }
                        }}
                        fullWidth={true}>
                        {props.firmwareList.map((item, index) => {
                            let selected = false;
                            if(selectedItem === item.id){
                                selected=true
                            }
                            return (
                            <MenuItem key={"firmware-"+ index} value={item.id} selected={selected}>{item.version + " | " + item.constructor}</MenuItem>
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
