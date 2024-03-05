import React, { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { Autocomplete, Box, Button, Container, Grid, Paper, TextField, Typography } from '@mui/material';
import TitleComponent from "./components/TitleComponent";
import FirmwareComponent from "./components/FirmwareComponent";
import DescriptionComponent from "./components/DescriptionComponent";
import KeyValuePairComponent from "./components/KeyValuePairComponent";
import {
    ErrorState,
    getConfiguration,
    getTranscriptors,
    GlobalState,
    KeyValueConfiguration,
    postNewConfiguration,
    postUpdateConfiguration,
    Transcriptor
} from "../../../conf/configurationController";


function AddKeyValuePair(props: {
    setSelectedKeys: React.Dispatch<React.SetStateAction<Transcriptor[]>>,
    selectedKeys: Transcriptor[],
}) {
    const {
        setSelectedKeys,
        selectedKeys,
    } = props;

    const [selectedKey, setSelectedKey] = useState<Transcriptor | null>(null);
    const [options, setOptions] = useState<Transcriptor[]>([]);

    useEffect(() => {
        getTranscriptors()
            .then(transcriptors => {
                if (transcriptors === undefined) {
                    return
                }
                setOptions(transcriptors.filter(key => !selectedKeys.map(selected => selected.id).includes(key.id)))
            })
        // setOptions(confKeys.filter(key => !selectedKeys.map(selected => selected.id).includes(key.id)));
    }, [selectedKeys])

    useEffect(() => {
        getTranscriptors()
            .then(transcriptors => {
                if (transcriptors === undefined) {
                    return
                }
                setOptions(transcriptors)
            })
    }, []);

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
                    disabled={options.length === 0}
                    onChange={(event, value) => {
                        setSelectedKey(value);
                    }}
                    sx={{width: 300}}
                    disablePortal
                    options={options}
                    getOptionLabel={option => option.fullName}
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
            <TitleComponent errorState={props.errorState} globalState={props.globalState}
                            setGlobalState={props.setGlobalState}/>
            <FirmwareComponent errorState={props.errorState} globalState={props.globalState}
                               setGlobalState={props.setGlobalState}/>
            <DescriptionComponent errorState={props.errorState} globalState={props.globalState}
                                  setGlobalState={props.setGlobalState}/>
        </Box>
    );
}

function RightSection(props: { globalState: GlobalState; setGlobalState: Dispatch<SetStateAction<GlobalState>> , selectedKeys: Transcriptor[], setSelectedKeys: Dispatch<SetStateAction<Transcriptor[]>> }) {
    const backgroundColor = 'rgb(249, 246, 251)'
    
    return (
        <Box>
            <Paper elevation={2} sx={{p: 2, pt: 0, mt: 3, backgroundColor}}>
                <Grid direction={"column"} container justifyContent="space-between">
                    <Grid xs={4} item>
                        <h4>Champs de la configuration :</h4>
                    </Grid>
                    <Grid xs={7} item>

                        <Box sx={{pt: 1, pb: 1}} style={{maxHeight: '60vh', overflow: 'auto'}}>
                            <AddKeyValuePair setSelectedKeys={props.setSelectedKeys} selectedKeys={props.selectedKeys}/>
                            {props.selectedKeys.length !== 0 && (
                                <Grid sx={{pt: 1, pb: 1}} direction="column" container alignItems="center"
                                      justifyContent="space-evenly">
                                    {props.selectedKeys.map((key) => {
                                        return (
                                            <KeyValuePairComponent
                                                key={key.id}
                                                value={/*props.globalState.configuration.find(v => v.key == key)?.value ??*/ ""}
                                                globalState={props.globalState}
                                                selectedKeys={props.selectedKeys}
                                                setSelectedKeys={props.setSelectedKeys}
                                                selectedKey={key}
                                                setGlobalState={props.setGlobalState}/>
                                        )
                                    })}
                                </Grid>
                            )}
                        </Box>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
}

function CreateConfig(props: {id?: number}) {

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
    
    const [selectedKeys, setSelectedKeys] = useState<Transcriptor[]>([]);
    const [loaded, setLoaded] = useState(false);

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
        if (!check(globalState)) {
            // If props.id not undefined then it's an update
            if(props.id){
                postUpdateConfiguration(props.id, globalState)
                return
            }
            postNewConfiguration(globalState) // manage response to display error or success
        }
    }

    // Fetch the configuration
    useEffect(() => {
        if(!props.id){
            setLoaded(true)
            return
        }
        getConfiguration(props.id).then(result => {
            if(!result){
                console.log("Erreur lors de la récupération de la configuration.")
                //setError("Erreur lors de la récupération de la configuration.")
                return
            }

            //console.log(config)
            getTranscriptors().then(transcriptors => {
                if (!transcriptors) {
                    return
                }
                let config: KeyValueConfiguration[] = Object.entries(JSON.parse(result.configuration)).map(([key, value]) => ({
                    key: transcriptors.find(t => t.id === Number(key)),
                    value: value,
                } as KeyValueConfiguration));

                var configurationKeys: number[] = config.map(conf => conf.key.id)
                setSelectedKeys(transcriptors.filter(key => configurationKeys.includes(key.id)))
                setGlobalState({
                    configuration: config,
                    description: result.description,
                    firmware: result.firmware.id + "",
                    name: result.name,
                })
                setLoaded(true)
            })
        });
    }, [props.id])

    return (
        <Box>
            {loaded && (
                <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
                <Grid container spacing={15}>
                    <Grid item xs={12} md={6}>
                        <LeftSection errorState={errorState} globalState={globalState} setGlobalState={setGlobalState}/>
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <RightSection globalState={globalState} setGlobalState={setGlobalState} selectedKeys={selectedKeys} setSelectedKeys={setSelectedKeys}/>
                    </Grid>
                </Grid>
                <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                    {/* TODO : fixer le bouton en bas */}
                    <Button sx={{borderRadius: 28}} onClick={handleSubmit} variant="contained"
                            color="primary">Valider</Button>
                </Box>
            </Container>
            )}
            {!loaded && (
                <Box maxWidth={"true"} maxHeight={"true"} justifyContent={"center"}>
                    <Typography variant='h5' textAlign={"center"}>Chargement des informations...</Typography>
                </Box>
            )}
        </Box>

    );
}

export default CreateConfig;
