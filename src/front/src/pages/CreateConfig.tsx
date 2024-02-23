import React, { useState } from 'react';
import { Autocomplete, Box, Button, Container, Grid, Input, MenuItem, Paper, Select, TextField } from '@mui/material';
import confKeys from "../conf/confKeys";
import Typography from "@mui/material/Typography";

const FirmwareSection = () => (
    <Box>
        <Paper elevation={2} sx={{p: 2, mt: 3}}>
            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={4} item>
                    <h4>Titre de la configuration : </h4>
                </Grid>
                <Grid xs={7} item>
                    <Input fullWidth={true} placeholder="Titre"/>
                </Grid>
            </Grid>
        </Paper>

        <Grid container alignItems="center" justifyContent="space-between">
            <Grid xs={4} item>
                <h4>Firmware : </h4>
            </Grid>
            <Grid xs={7} item>
                <Paper elevation={2} sx={{p: 2, mt: 3}}>
                    <Select defaultValue={6.7} fullWidth={true}>
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
                    <Input multiline minRows={4} maxRows={6} fullWidth={true}
                           placeholder="Description de la configuration"/>
                </Grid>
            </Grid>
        </Paper>
    </Box>
);


function KeyValuePair(props: {
    selectedKey: string
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
                            setCurrentValue(event.target.value)
                            console.log(selectedKey, event.target.value)
                        }}
                        value={currentValue}
                        placeholder="valeur"/>
                </Grid>
            </Grid>
        </Grid>
    )
}

function KeyValueSection() {
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
                            <Input defaultValue="BRS-HERR" placeholder="valeur"/>
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
                                    <KeyValuePair key={key} selectedKey={key} />
                                )
                            })}
                        </Grid>
                    </Paper>
                )}
            </Box>
        </Box>
    );
}

const FirmwareUpdate = () => {
    return (
        <Container maxWidth="xl" sx={{mt: 4, mb: 4}}>
            <Grid container spacing={15}>
                <Grid item xs={12} md={6}>
                    <FirmwareSection/>
                </Grid>
                <Grid item xs={12} md={6}>
                    <KeyValueSection/>
                </Grid>
            </Grid>
            <Box sx={{display: 'flex', justifyContent: 'center', mt: 4}}>
                <Button variant="contained" color="primary">Valider</Button>
            </Box>
        </Container>
    );
};

export default FirmwareUpdate;