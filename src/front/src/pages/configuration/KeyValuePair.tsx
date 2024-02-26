import { Configuration, GlobalState } from "./GlobalState";
import React, { Dispatch, SetStateAction, useState } from "react";
import { Button, Grid, Input } from "@mui/material";
import Typography from "@mui/material/Typography";

function KeyValuePair(props: {
    selectedKey: string,
    setGlobalState: Dispatch<SetStateAction<GlobalState>>,
    setSelectedKeys: React.Dispatch<React.SetStateAction<string[]>>,
    selectedKeys: string[]
}): JSX.Element {
    const {
        selectedKey,
        setGlobalState,
        setSelectedKeys,
        selectedKeys,
    } = props;

    const [currentValue, setCurrentValue] = useState("");

    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const newValue = event.target.value
        setCurrentValue(newValue)
        const newKey: Configuration = {
            key: selectedKey,
            value: newValue
        }
        setGlobalState(prevState => {
            let updated = false;
            prevState.configuration.forEach(conf => {
                if (conf.key === newKey.key) {
                    conf.value = newKey.value
                    updated = true
                }
            })
            if (!updated) {
                prevState.configuration.push(newKey)
            }
            return {
                configuration: prevState.configuration,
                firmware: prevState.firmware,
                description: prevState.description,
                name: prevState.name
            }
        })
    }

    return (
        <Grid sx={{pt: 1, pb: 1}} container alignItems="center">
            <Grid item sm={4}>
                <Typography>{selectedKey}</Typography>
            </Grid>
            <Grid item sm={1}>
                <p>:</p>
            </Grid>
            <Grid item sm={5}>
                <Input
                    onChange={handleChange}
                    value={currentValue}
                    placeholder="valeur"/>
            </Grid>
            <Button
                size={"large"}
                sx={{
                    width: "30px", // Adjust as needed
                    height: "30px", // Adjust as needed
                    color: "error",
                }}
                color={"error"}
                onClick={() => {
                    setSelectedKeys(selectedKeys.filter(key => key !== selectedKey))
                }}
            >
                <h2>&times;</h2>
            </Button>
        </Grid>
    )
}

export default KeyValuePair;