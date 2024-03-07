import React, {useEffect, useState} from "react";
import {Button, Container, Grid, IconButton, MenuItem, Paper, Select} from "@mui/material";
import FormInput from "../../../sharedComponents/FormInput";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import {Configuration, getAllConfigurations, noConfig} from "../../../conf/configurationController";
import {
    CreateChargepointDto,
    getChargepointById,
    postNewChargepoint,
    updateChargepoint
} from "../../../conf/chargePointController";
import {SkeletonChargepoint} from "./components/SkeletonChargepoint";
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import { useNavigate } from "react-router";

function DisplayConfiguration({configuration}: { configuration?: Configuration }) {

    return (
        <Box sx={{height: "100%"}}>
            <Typography sx={{mt: 1}} variant={"h6"}>Configuration sélectionnée :</Typography>
            <Paper elevation={0}
                   variant={"outlined"}
                   sx={{
                       p: 1, pt: 1, mt: 2, mb: 2,
                       backgroundColor: 'rgb(247, 244, 249)',
                       borderColor: 'rgba(226,229,233,255)',
                       borderWidth: 2,
                       overflow: "auto",
                       height: "65vh"
                   }}
            >
                <Box sx={{color: 'rgb(130,130,130)'}}>
                    {configuration !== undefined && configuration.id !== -1 &&
                        <>
                            <Typography sx={{p: 0, pl: 2, mt: 0, fontStyle: 'italic'}}>
                                Dernière modification : {new Date(configuration.lastEdit).toLocaleString()}
                            </Typography>
                            <Typography sx={{p: 2, pt: 0, mt: 0, fontWeight: 'bold'}}>{configuration.name}</Typography>
                            <Typography sx={{p: 2, pt: 0, mt: 0}}>
                                Version du firmware : {configuration.firmware.version}
                            </Typography>
                            <Typography sx={{p: 2, pt: 0, mt: 0}}>
                                <pre>
                                    {JSON.stringify(JSON.parse(configuration.configuration || "{}"), null, 2)}
                                </pre>
                            </Typography>
                            <Typography sx={{p: 2, pt: 0, mt: 0}}>
                                "{configuration.description}"
                            </Typography>
                        </>
                    }
                </Box>
            </Paper>
        </Box>
    )
}

function CreateChargepoint(props: { id?: number }) {

    const [configurationList, setConfigurationList] = useState<Configuration[]>([]);

    const [serialNumber, setSerialNumber] = useState<string>("")
    const [type, setType] = useState<string>("")
    const [constructor, setConstructor] = useState<string>("")
    const [clientId, setClientId] = useState<string>("")
    const [configuration, setConfiguration] = useState<Configuration>(noConfig);
    const [loading, setLoading] = useState(true);

    const [chargepoint, setChargepoint] = useState<CreateChargepointDto>({
        serialNumber: serialNumber,
        type: type,
        constructor: constructor,
        clientId: clientId,
        configuration: configuration.id,
    });

    const navigate = useNavigate();

    useEffect(() => {
        if (!props.id) {
            return
        }
        getChargepointById(props.id).then(result => {
            if (result === undefined) {
                return
            }
            console.log(result)
            setSerialNumber(result.serialNumberChargepoint)
            setType(result.type)
            setConstructor(result.constructor)
            setClientId(result.clientId)

            const config = result.configuration ? result.configuration : noConfig
            setConfiguration(config)
            setChargepoint({
                serialNumber: result.serialNumberChargepoint,
                type: result.type,
                constructor: result.constructor,
                clientId: result.clientId,
                configuration: config.id
            })
        })
    }, [props.id]);

    useEffect(() => {
        getAllConfigurations().then((result) => {
            setLoading(false)
            if (result === undefined) {
                return
            }
            // to display the choice in the list
            setConfigurationList([noConfig, ...result])
        })
    }, []);

    return (
        <Grid>
            <Grid item xs={12}>
                <IconButton
                    onClick={() => navigate("/home/account")}
                >
                    <ArrowBackIosNewIcon/>
                </IconButton>
            </Grid>
            <Container maxWidth="xl" sx={{mb: 4}}>
                <Grid container spacing={15}>
                    <Grid item xs={12} md={6}>
                        <Box>
                            {loading ? (
                                <SkeletonChargepoint />
                            ) : (
                                <>
                                    <FormInput name={"N° Série"}
                                               onChange={val => {
                                                   setSerialNumber(val)
                                                   setChargepoint(prevState => {
                                                       prevState.serialNumber = val
                                                       return prevState
                                                   })
                                               }}
                                               value={serialNumber}
                                               checkIsWrong={value => value === "abc"}
                                    />
                                    <FormInput name={"Client ID"}
                                               onChange={val => {
                                                   setClientId(val)
                                                   setChargepoint(prevState => {
                                                       prevState.clientId = val
                                                       return prevState
                                                   })
                                               }}
                                               value={clientId}
                                               checkIsWrong={value => value === "abc"}
                                    />
                                    <FormInput name={"Constructeur"}
                                               onChange={val => {
                                                   setConstructor(val)
                                                   setChargepoint(prevState => {
                                                       prevState.constructor = val
                                                       return prevState
                                                   })
                                               }}
                                               value={constructor}
                                               checkIsWrong={value => value === "abc"}
                                    />
                                    <FormInput name={"Modèle"}
                                               onChange={val => {
                                                   setType(val)
                                                   setChargepoint(prevState => {
                                                       prevState.type = val
                                                       return prevState
                                                   })
                                               }}
                                               value={type}
                                               checkIsWrong={value => value === "abc"}
                                    />
                                    <Paper elevation={2} sx={{p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}>
                                        <Grid container alignItems="center" justifyContent="space-between">
                                            <Grid xs={4} item>
                                                <Typography sx={{mt: 1}} variant={"h6"}>Configuration :</Typography>
                                            </Grid>
                                            <Grid xs={8} item>
                                                <Select
                                                    value={configuration?.id}
                                                    onChange={event => {
                                                        const val = event.target.value as number;
                                                        setConfiguration(configurationList.find(conf => conf.id === val) as Configuration);
                                                        setChargepoint(prevState => {
                                                            prevState.configuration = val;
                                                            return prevState;
                                                        });
                                                    }}
                                                    fullWidth
                                                >
                                                    {configurationList?.map(configuration => (
                                                        <MenuItem key={configuration.id} value={configuration.id}>
                                                            {configuration.name}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </Grid>
                                        </Grid>
                                    </Paper>
                                    <Box
                                        sx={{
                                            display: 'flex',
                                            justifyContent: 'center',
                                            boxSizing: 'border-box',
                                        }}
                                        pt={2}
                                    >
                                        <Button sx={{borderRadius: 28}} variant="contained" color="primary"
                                                onClick={handleSubmit}>Valider</Button>
                                    </Box>
                                </>
                            )}
                        </Box>
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <DisplayConfiguration configuration={configuration}/>
                    </Grid>
                </Grid>
            </Container>
        </Grid>
    );

    function handleSubmit() {
        if (props.id) {
            return updateChargepoint(props.id, chargepoint);
        } else {
            return postNewChargepoint(chargepoint);
        }
    }
}

export default CreateChargepoint;
