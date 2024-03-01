import React, { useEffect, useState } from "react";
import { Button, Container, Grid, MenuItem, Paper, Select } from "@mui/material";
import FormInput from "../FormInput";
import Box from "@mui/material/Box";
import Typography from "@mui/material/Typography";
import { Configuration, getAllConfigurations, noConfig } from "../../conf/configurationController";
import { CreateChargepointDto, postNewChargepoint } from "../../conf/chargePointController";

function DisplayConfiguration({configuration}: { configuration: Configuration | undefined }) {

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

function CreateChargepoint() {

    const [configurationList, setConfigurationList] = useState<Configuration[]>([]);
    const [configuration, setConfiguration] = useState<Configuration>(noConfig);
    const [chargepoint, setChargepoint] = useState<CreateChargepointDto>({
        serialNumber: "",
        type: "",
        constructor: "",
        clientId: "",
        configuration: noConfig.id,
    });


    useEffect(() => {
        getAllConfigurations().then((result) => {
            if (result === undefined) {
                return
            }
            setConfigurationList([noConfig, ...result])
        })
    }, []);

    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <Box>
                        <FormInput name={"N° Série"}
                                   onChange={val => setChargepoint(prevState => {
                                       prevState.serialNumber = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                        <FormInput name={"Client ID"}
                                   onChange={val => setChargepoint(prevState => {
                                       prevState.clientId = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                        <FormInput name={"Constructeur"}
                                   onChange={val => setChargepoint(prevState => {
                                       prevState.constructor = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                        <FormInput name={"Modèle"}
                                   onChange={val => setChargepoint(prevState => {
                                       prevState.type = val
                                       return prevState
                                   })}
                                   checkIsWrong={value => value === "abc"}
                        />
                        <Paper elevation={2} sx={{p: 2, mt: 3, backgroundColor: 'rgb(249, 246, 251)'}}>

                            <Grid container alignItems="center" justifyContent="space-between">
                                <Grid xs={4} item>
                                    <Typography sx={{mt: 1}} variant={"h6"}>Configuration :</Typography>
                                </Grid>
                                <Grid xs={8} item>
                                    <Select
                                        value={configuration.id}
                                        onChange={event => {
                                            const val = event.target.value as number
                                            setChargepoint(prevState => {
                                                prevState.configuration = val
                                                return prevState
                                            })
                                            setConfiguration(configurationList.find(conf => conf.id === val) as Configuration)
                                        }}
                                        fullWidth={true}>
                                        {configurationList && configurationList.map((configuration) => (
                                            <MenuItem key={configuration.id} value={configuration.id}
                                                      selected={true}>{configuration.name}</MenuItem>
                                        ))}
                                    </Select>
                                </Grid>
                            </Grid>
                        </Paper>
                    </Box>
                    <Box
                        sx={{
                            display: 'flex',
                            justifyContent: 'center',
                            boxSizing: 'border-box',
                        }}
                        pt={2}
                    >
                        <Button sx={{borderRadius: 28}} variant="contained" color="primary"
                                onClick={() => postNewChargepoint(chargepoint)}>Valider</Button>
                    </Box>

                </Grid>
                <Grid item xs={12} md={6}>
                    <DisplayConfiguration configuration={configuration}/>
                </Grid>
            </Grid>
        </Container>
    );
}

export default CreateChargepoint;
