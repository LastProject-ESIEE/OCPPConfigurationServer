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

function CreateConfig(props: { id?: number }) {
    const [errorState, setErrorState] = useState<ErrorState>({
        name: "",
        description: "",
        firmware: ""
    })
    const [title, setTitle] = useState("");
    const [firmware, setFirmware] = useState("");
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

        if (firmware.length === 0) {
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
                firmware: firmware,
            }
            // If props.id not undefined then it's an update
            if (props.id) {
                postUpdateConfiguration(props.id, resultData)
                return
            }
            postNewConfiguration(resultData) // manage response to display error or success
        }
    }


    // Fetch the configuration
    useEffect(() => {
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
            setKeys(items)

            if (!props.id) {
                setLoading(false)
                return
            }
            // If props.id is defined then it's an update
            getConfiguration(props.id).then(result => {
                if (!result) {
                    return
                }
                let config: KeyValueConfiguration[] = Object.entries(JSON.parse(result.configuration)).map(([key, value]) => ({
                    key: transcriptors.find(t => t.id === Number(key)),
                    value: value,
                } as KeyValueConfiguration));

                var configurationKeys: number[] = config.map(conf => conf.key.id)
                setSelectedKeys(items.filter(transcriptor => configurationKeys.includes(transcriptor.item.id)))
                setTitle(result.name)
                setFirmware(result.firmware.version)
                setDescription(result.description)
                setLoading(false)
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
                                    <FirmwareComponent errorState={errorState} value={firmware} setValue={setFirmware}/>
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
                                onClick={() => {
                                    if (!check()) {
                                        wsManager.emitNotification({
                                            type: "SUCCESS",
                                            title: "Succès ",
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
                                    handleSubmit();
                                }}
                            >Valider</Button>
                        </Box>
                    </>
                )}
            </Container>
        </Box>

    );
}

export default CreateConfig;
