//import { useState } from "react";



/*
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
*/