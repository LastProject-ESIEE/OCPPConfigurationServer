import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { Autocomplete, Box, Button, Container, Grid, Input, MenuItem, Paper, Select, TextField } from '@mui/material';
import confKeys from "../conf/confKeys";
import Typography from "@mui/material/Typography";


export async function postNewConfiguration(configuration: GlobalState): Promise<boolean> {
    let request = await fetch(window.location.origin + "/api/configuration/create",
        {
            method: "POST",
            body: JSON.stringify({
                name: configuration.name,
                description: configuration.description,
                configuration: JSON.stringify(configuration.configuration),
                firmware: configuration.firmware
            })
        })
    if(request.ok){
        return true
    }else{
        console.log("Fetch configuration list failed, error code:" +  request.status)
        return false
    }
}

type Firmware = {
    id: number,
    url: string,
    version: string,
    constructor: string,
}

function FirmwareSection (props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>
}) {
    const [firmware, setFirmware] = useState("");
    const [firmwares, setFirmwares] = useState<Firmware[]>([]);

    useEffect(() => {
        fetch("api/firmware/all")
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw response;
            })
            .then(data => {
                console.log("firmwares : ", data)
                setFirmwares(data);
            })
            .catch(error => {
                console.error("ERROR ", error);
            });
    }, []);

    return (
        <Box>
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

            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Firmware : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Paper elevation={2} sx={{p: 2, mt: 3}}>
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
                            <MenuItem value={6.7} selected={true}>6.7</MenuItem>
                            <MenuItem value={1.2} selected={true}>1.2</MenuItem>
                        </Select>
                    </Paper>
                </Grid>
            </Grid>


            <Paper elevation={2} sx={{p: 2, mt: 3}}>
                <Grid container alignItems="center" justifyContent="space-between">
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
        </Box>
    );
}


function KeyValuePair(props: {
    selectedKey: string,
    globalState: GlobalState,
    setGlobalState: Dispatch<SetStateAction<GlobalState>>
}): JSX.Element {
    const {selectedKey} = props;

    const [currentValue, setCurrentValue] = useState("");

    return (
        <Grid sx={{pt: 1, pb: 1}} container alignItems="center" justifyContent="space-evenly">
            <Grid sx={{pt: 1, pb: 1}} container alignItems="center" justifyContent="space-evenly">
                <Grid item>
                    <Typography>{selectedKey}</Typography>
                </Grid>
                <Grid item>
                    <p>:</p>
                </Grid>
                <Grid item>
                    <Input
                        onChange={event => {
                            const newValue = event.target.value
                            setCurrentValue(newValue)
                            const newKey: Configuration = {
                                key: props.selectedKey,
                                value: newValue
                            }
                            props.setGlobalState(prevState => {
                                let updated = false;
                                prevState.configuration.forEach(conf => {
                                    if(conf.key === newKey.key){
                                        conf.value = newKey.value
                                        updated = true
                                    }
                                })
                                if(!updated){
                                    prevState.configuration.push(newKey)
                                }
                                return {
                                    configuration: prevState.configuration,
                                    firmware: prevState.firmware,
                                    description: prevState.description,
                                    name: prevState.name
                                }
                            })
                        }}
                        value={currentValue}
                        placeholder="valeur"/>
                </Grid>
            </Grid>
        </Grid>
    )
}

function KeyValueSection(props: { globalState: GlobalState; setGlobalState: Dispatch<SetStateAction<GlobalState>> }) {
    const [options, setOptions] = useState(confKeys.map(key => key.keyName));
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);
    const [selectedKey, setSelectedKey] = useState<string | null>(null);

    const updateOptions = () => {
        if (selectedKey === null) {
            return; // Handle potential error or prevent unnecessary updates
        }

        const newSelectedKeys = [selectedKey, ...selectedKeys];

        setSelectedKey(null);
        setOptions(options.filter(key => !newSelectedKeys.includes(key)));
        setSelectedKeys(newSelectedKeys);
    };

    return (
        <Box>
            <h4>Clé: valeur :</h4>
            <Box sx={{pt: 1, pb: 1}} style={{maxHeight: '60vh', overflow: 'auto'}}>
                <Paper elevation={2} sx={{p: 2}}>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                            <Input disabled defaultValue="Borne_id" placeholder="Borne_id"/>
                        </Grid>
                        <Grid item>
                            <p>:</p>
                        </Grid>
                        <Grid item>
                            <Input defaultValue="BRS-HERR" onChange={ev => {
                                const newKey: Configuration = {
                                    key:"Identity",
                                    value:ev.target.value
                                }
                                props.setGlobalState(prevState => {
                                    let updated = false;
                                    prevState.configuration.forEach(conf => {
                                        if(conf.key === newKey.key){
                                            conf.value = newKey.value
                                            updated = true
                                        }
                                    })
                                    if(!updated){
                                        prevState.configuration.push(newKey)
                                    }
                                    return {
                                        configuration: prevState.configuration,
                                        firmware: prevState.firmware,
                                        description: prevState.description,
                                        name: prevState.name
                                    }
                                })

                            }} placeholder="valeur"/>
                        </Grid>
                    </Grid>
                </Paper>

                <Paper elevation={2} sx={{p: 2}}>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                            <Autocomplete
                                onChange={(event, value) => {
                                    setSelectedKey(value);
                                }}
                                sx={{width: 300}}
                                disablePortal
                                options={options}
                                value={selectedKey}
                                renderInput={(params) => <TextField {...params} label="Clé"/>}
                            />
                        </Grid>
                        <Grid item>
                            <Button onClick={updateOptions} variant="contained" type="submit" sx={{borderRadius: 28}}>
                                +
                            </Button>
                        </Grid>
                    </Grid>
                </Paper>
                {selectedKeys.length !== 0 && (
                    <Paper elevation={2} sx={{p: 2, mt: 2}}>
                        <Grid sx={{pt: 1, pb: 1}} container alignItems="center" justifyContent="space-evenly">
                            {selectedKeys.map((key) => {
                                return (
                                    <KeyValuePair key={key} selectedKey={key} globalState={props.globalState} setGlobalState={props.setGlobalState}/>
                                )
                            })}
                        </Grid>
                    </Paper>
                )}
            </Box>
        </Box>
    );
}

type GlobalState = {
    name: string,
    description: string,
    configuration: Configuration[],
    firmware: string
}

type Configuration = {
    key: string,
    value: string,
}

const FirmwareUpdate = () => {

    const [globalState, setGlobalState] = useState<GlobalState>({
        name: "",
        description: "",
        configuration: [],
        firmware: ""
    })

    function handleSubmit() {
        console.log(globalState)
        postNewConfiguration(globalState) // manage response to display error or succes
    }

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <FirmwareSection globalState={globalState} setGlobalState={setGlobalState}/>
                </Grid>
                <Grid item xs={12} md={6}>
                    <KeyValueSection globalState={globalState} setGlobalState={setGlobalState}/>
                </Grid>
            </Grid>
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Button onClick={handleSubmit} variant="contained" color="primary">Valider</Button>
            </Box>
        </Container>
    );
};

export default FirmwareUpdate;