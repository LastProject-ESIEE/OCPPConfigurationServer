import {useEffect, useState} from 'react';
import {Box, Button, Container, Grid} from '@mui/material';
import TitleComponent from "./components/TitleComponent";
import FirmwareComponent from "./components/FirmwareComponent";
import DescriptionComponent from "./components/DescriptionComponent";
import {
    CreateConfigurationData,
    ErrorState,
    getConfiguration,
    getTranscriptors,
    KeyValueConfiguration,
    postNewConfiguration,
    postUpdateConfiguration,
    Transcriptor
} from "../../../conf/configurationController";
import SelectItemsList, {KeyValueItem} from '../../../sharedComponents/SelectItemsList';
import {SkeletonConfiguration} from "./components/SkeletonConfiguration";
import BackButton from '../../../sharedComponents/BackButton';
import {useNavigate} from "react-router";
import {wsManager} from "../Home";
import { Firmware } from '../../../conf/FirmwareController';
import { getAllElements } from '../../../conf/backendController';

function CreateConfig(props: { id?: number }) {
    const [errorState, setErrorState] = useState<ErrorState>({
        name: "",
        description: "",
        firmware: ""
    })
    const [title, setTitle] = useState("");
    const [firmwareList, setFirmwareList] = useState<Firmware[]>([]);
    const [firmware, setFirmware] = useState<Firmware | undefined>(undefined);
    const [description, setDescription] = useState("");
    const [keys, setKeys] = useState<KeyValueItem<Transcriptor>[]>([]);
    const [selectedKeys, setSelectedKeys] = useState<KeyValueItem<Transcriptor>[]>([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    function check(): boolean {
        setErrorState({
            name: "",
            description: "",
            firmware: ""
        })

        let error = false;
        if (title.length === 0) {
            setErrorState((prevState) => ({
                ...prevState,
                name: "Le titre est obligatoire.",
            }));
            error = true;
        }

        if (title.length > 50) {
            setErrorState((prevState) => ({
                ...prevState,
                name: "Le titre ne peut pas dépasser 50 caractères.",
            }));
            error = true;
        }

        if (firmware === undefined) {
            setErrorState((prevState) => ({
                ...prevState,
                firmware: "Le firmware est obligatoire.",
            }));
            error = true;
        }

        return error;
    }

    function handleSubmit() {
        if (!check()) {
            let resultData: CreateConfigurationData = {
                name: title,
                configuration: selectedKeys.map(keyValueTranscriptor => {
                    return {
                        key: keyValueTranscriptor.item,
                        value: keyValueTranscriptor.value,
                    }
                }),
                description: description,
                firmware: firmware ? firmware.id + "" : "",
            }
            // If props.id not undefined then it's an update
            if (props.id) {
                postUpdateConfiguration(props.id, resultData).then(value => {
                    if (value) {
                        wsManager.emitNotification({
                            type: "INFO",
                            title: title + " ",
                            content: "La configuration a été modifiée."
                        });
                        navigate("/home/configuration");
                    } else {
                        wsManager.emitNotification({
                            type: "ERROR",
                            title: "Erreur ",
                            content: "La configuration n'a pas pu être modifiée."
                        })
                    }
                })
                return
            }
            postNewConfiguration(resultData).then(value => {
                if (value) {
                    wsManager.emitNotification({
                        type: "SUCCESS",
                        title: title + " ",
                        content: "La configuration a été créée."
                    });
                    navigate("/home/configuration");
                } else {
                    wsManager.emitNotification({
                        type: "ERROR",
                        title: "Erreur ",
                        content: "La configuration n'a pas pu être créée."
                    })
                }
            })
        }
    }

    // Fetch the configuration
    useEffect(() => {
        // Fetch firmware list
        getAllElements<Firmware>("/api/firmware/all").then(result => {
            if(!result){
                console.error("Failed to fetch firmware list.")
                return
            }
            setFirmwareList(result)
        })
        // Fetch transcriptors and current configuration
        getTranscriptors().then(transcriptors => {
            if (!transcriptors) {
                return
            }
            // Load transcriptors
            let items: KeyValueItem<Transcriptor>[] = transcriptors.map(transcriptor => {
                    return {
                        id: transcriptor.id + "",
                        checker: item => {
                            return true;//RegExp(transcriptor.regex).exec(item)
                        },
                        item: transcriptor,
                        label: transcriptor.fullName,
                        value: ""
                    }
                }
            )
            
            // If not not an update skip the next step
            if (!props.id) {
                setKeys(items)
                setLoading(false)
                return
            }
            // If props.id is defined then it's an update
            getConfiguration(props.id).then(result => {
                if (!result) {
                    return
                }
                // Transform data and retrieve the transcriptor for each configuration fields
                let config: KeyValueConfiguration[] = Object.entries(JSON.parse(result.configuration)).map(([key, value]) => {
                    let configurationItem = items.find(item => item.id === key)
                    if (configurationItem) {
                        configurationItem.value = (value as string)
                    } else {
                        console.warn("A configuration field in the configuration is not set in the configuration field list.")
                    }
                    return {
                        key: transcriptors.find(t => t.id === Number(key)),
                        value: value,
                    } as KeyValueConfiguration
                })
                var configurationKeys: number[] = config.map(conf => conf.key.id)
                setSelectedKeys(items.filter(transcriptor => configurationKeys.includes(transcriptor.item.id)))
                setTitle(result.name)
                setFirmware(result.firmware)
                setDescription(result.description)
                setLoading(false)
                setKeys(items)
            });
        })
    }, [props.id])

    return (
        <Box>
            <BackButton link={"/home/configuration"} top={11}/>
            <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
                {loading ? (
                    <SkeletonConfiguration />
                ) : (
                    <>
                        <Grid container spacing={15}>
                            <Grid item xs={12} md={6}>
                                <Box>
                                    <TitleComponent errorState={errorState} value={title} setValue={setTitle}/>
                                    <FirmwareComponent 
                                        errorState={errorState} 
                                        current={firmware} 
                                        firmwareList={firmwareList} 
                                        onSelectionChange={value => setFirmware(value)}
                                    />
                                    <DescriptionComponent errorState={errorState} value={description}
                                                          setValue={setDescription}/>
                                </Box>
                            </Grid>
                            <Grid item xs={12} md={6}>
                                {/*<RightSection globalState={globalState} setGlobalState={setGlobalState} selectedKeys={selectedKeys} setSelectedKeys={setSelectedKeys} />*/}
                                <SelectItemsList
                                    title='Champs de la configuration'
                                    keyTitle='Clés'
                                    items={keys}
                                    selectKind='input'
                                    selectedItems={selectedKeys}
                                    setSelectedItems={setSelectedKeys}
                                />
                            </Grid>
                        </Grid>
                        <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                            {/* TODO : fixer le bouton en bas */}
                            <Button
                                sx={{borderRadius: 28}}
                                variant="contained"
                                color="primary"
                                onClick={handleSubmit}
                            >
                                Valider
                            </Button>
                        </Box>
                    </>
                )}
            </Container>
        </Box>
    );
}

export default CreateConfig;
