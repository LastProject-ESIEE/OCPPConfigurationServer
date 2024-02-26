import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { Autocomplete, Box, Button, Container, Grid, Paper, TextField } from '@mui/material';
import confKeys from "../../conf/confKeys";
import { ErrorState, GlobalState } from "./GlobalState";
import TitleComponent from "./TitleComponent";
import FirmwareComponent from "./FirmwareComponent";
import DescriptionComponent from "./DescriptionComponent";
import KeyValuePair from "./KeyValuePair";


export async function postNewConfiguration(configuration: GlobalState): Promise<boolean> {
    let myConfig = configuration.configuration.map(keyValue => `"${keyValue.key}":"${keyValue.value}"`)
        .join(", ")

    myConfig = "{" + myConfig + "}"

    let request = await fetch(window.location.origin + "/api/configuration/create",
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                name: configuration.name,
                description: configuration.description,
                configuration: myConfig,
                firmware: configuration.firmware
            })
        })
    if (request.ok) {
        return true
    } else {
        console.log("Fetch configuration list failed, error code:" + request.status)
        return false
    }
}

function AddKeyValuePair(props: {
    setSelectedKeys: React.Dispatch<React.SetStateAction<string[]>>,
    selectedKeys: string[],
}) {
    const {
        setSelectedKeys,
        selectedKeys,
    } = props;

    const [selectedKey, setSelectedKey] = useState<string | null>(null);
    const [options, setOptions] = useState(confKeys.map(key => key.keyName));

    useEffect(() => {
        setOptions(confKeys.map(key => key.keyName).filter(key => !selectedKeys.includes(key)));
    }, [selectedKeys])

    const updateOptions = () => {
        if (selectedKey === null) {
            return; // Handle potential error or prevent unnecessary updates
        }

        const newSelectedKeys = [selectedKey, ...selectedKeys];

        setSelectedKey(null);
        // setOptions(options.filter(key => !newSelectedKeys.includes(key)));
        setSelectedKeys(newSelectedKeys);
    };

    return (
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
                <Button size={"large"} onClick={updateOptions} variant="contained" type="submit"
                        sx={{borderRadius: 100}}>
                    <span style={{fontSize: "larger", fontWeight: "bolder"}}>+</span>
                </Button>
            </Grid>
        </Grid>
    )
}

function LeftSection(props: {
    globalState: GlobalState;
    setGlobalState: Dispatch<SetStateAction<GlobalState>>,
    errorState: ErrorState
}) {
    return (
        <Box>
            <TitleComponent errorState={props.errorState} globalState={props.globalState} setGlobalState={props.setGlobalState}/>
            <FirmwareComponent errorState={props.errorState} globalState={props.globalState} setGlobalState={props.setGlobalState}/>
            <DescriptionComponent errorState={props.errorState} globalState={props.globalState} setGlobalState={props.setGlobalState}/>
        </Box>
    );
}

function RightSection(props: { globalState: GlobalState; setGlobalState: Dispatch<SetStateAction<GlobalState>> }) {
    const [selectedKeys, setSelectedKeys] = useState<string[]>([]);

    return (
        <Box>
            <Paper elevation={2} sx={{p: 2, mt: 2}}>
                <h4>Champs de la configuration :</h4>
                <Box sx={{pt: 1, pb: 1}} style={{maxHeight: '60vh', overflow: 'auto'}}>
                    <AddKeyValuePair setSelectedKeys={setSelectedKeys} selectedKeys={selectedKeys}/>
                    {selectedKeys.length !== 0 && (
                        <Grid sx={{pt: 1, pb: 1}} direction="column" container alignItems="center"
                              justifyContent="space-evenly">
                            {selectedKeys.map((key) => {
                                return (
                                    <KeyValuePair
                                        key={key}
                                        selectedKeys={selectedKeys}
                                        setSelectedKeys={setSelectedKeys}
                                        selectedKey={key}
                                        setGlobalState={props.setGlobalState}/>
                                )
                            })}
                        </Grid>
                    )}
                </Box>
            </Paper>
        </Box>
    );
}

function FirmwareUpdate() {

    const [globalState, setGlobalState] = useState<GlobalState>({
        name: "",
        description: "",
        configuration: [],
        firmware: ""
    })

    const [errorState, setErrorState] = useState<ErrorState>({
        name: "",
        description: "",
        firmware: ""
    })

    function check(globalState: GlobalState): boolean {
        setErrorState({
            name: "",
            description: "",
            firmware: ""
        })

        let error = false;
        if (globalState.name.length === 0) {
            setErrorState((prevState) => ({
                ...prevState,
                name: "Le titre est obligatoire.",
            }));
            error = true;
        }

        if (globalState.name.length > 50) {
            setErrorState((prevState) => ({
                ...prevState,
                name: "Le titre ne peut pas dépasser 50 caractères.",
            }));
            error = true;
        }

        if (globalState.firmware.length === 0) {
            setErrorState((prevState) => ({
                ...prevState,
                firmware: "Le firmware est obligatoire.",
            }));
            error = true;
        }

        return error;
    }

    function handleSubmit() {
        console.log(globalState)
        if (check(globalState)) {
            // postNewConfiguration(globalState) // manage response to display error or success
        }
    }

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <LeftSection errorState={errorState} globalState={globalState} setGlobalState={setGlobalState}/>
                </Grid>
                <Grid item xs={12} md={6}>
                    <RightSection globalState={globalState} setGlobalState={setGlobalState}/>
                </Grid>
            </Grid>
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                {/* TODO : fixer le bouton en bas */}
                <Button sx={{borderRadius: 28}} onClick={handleSubmit} variant="contained"
                        color="primary">Valider</Button>
            </Box>
        </Container>
    );
}

export default FirmwareUpdate;