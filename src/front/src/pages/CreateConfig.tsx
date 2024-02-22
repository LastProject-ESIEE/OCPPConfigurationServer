import React, { SyntheticEvent, useState } from 'react';
import {
    Autocomplete,
    AutocompleteChangeDetails,
    AutocompleteChangeReason,
    Box,
    Button,
    Container,
    Grid,
    Input,
    MenuItem,
    Paper,
    Select,
    TextField
} from '@mui/material';
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
    selectedKeys: Set<string>,
    setSelectedKeys: React.Dispatch<React.SetStateAction<Set<string>>>,
    options: string[],
    setOptions: React.Dispatch<React.SetStateAction<string[]>>
}): JSX.Element {
    const {selectedKeys, setSelectedKeys, options, setOptions} = props;

    const [currentValue, setCurrentValue] = useState("");

    function handleChange(event: SyntheticEvent<Element, Event>, newValue: string | null, reason: AutocompleteChangeReason, details?: AutocompleteChangeDetails<string> | undefined) {
        const deleteSelection = () => {
            selectedKeys.delete(currentValue);
            setSelectedKeys(selectedKeys);
            setOptions(confKeys
                .filter(key => !selectedKeys.has(key.keyName))
                .map(key => key.keyName))
        }
        if (newValue === null) {
            return;
        }

        deleteSelection();
        if (reason === "clear" || !options.includes(newValue)) {
            return;
        }
        setCurrentValue(newValue);
        setSelectedKeys(selectedKeys.add(newValue));
        setOptions(confKeys
            .filter(key => !selectedKeys.has(key.keyName))
            .map(key => key.keyName));

    }

    return (
        <Grid sx={{pt: 1, pb: 1}} container alignItems="center" justifyContent="space-evenly">
            <Grid item>
                <Autocomplete
                    onChange={handleChange}
                    sx={{width: 300}}
                    disablePortal
                    options={options}
                    renderInput={(params) => <TextField {...params} label="Clé"/>}/>
            </Grid>
            <Grid item>
                <p>: </p>
            </Grid>
            <Grid item>
                <Input placeholder="valeur"/>
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

        const newSelectedKeys = [...selectedKeys, selectedKey];

        setSelectedKey(null);
        setOptions(options.filter(key => !newSelectedKeys.includes(key)));
        setSelectedKeys(newSelectedKeys);
    };

    return (
        <Box>
            <h4>Clé: valeur :</h4>
            <Box sx={{ pt: 1, pb: 1 }} style={{ maxHeight: '60vh', overflow: 'auto' }}>
                <Paper elevation={2} sx={{ p: 2 }}>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                            <Input disabled defaultValue="Borne_id" placeholder="Borne_id" />
                        </Grid>
                        <Grid item>
                            <p>:</p>
                        </Grid>
                        <Grid item>
                            <Input defaultValue="BRS-HERR" placeholder="valeur" />
                        </Grid>
                    </Grid>
                </Paper>

                <Paper elevation={2} sx={{ p: 2 }}>
                    <Grid container alignItems="center" justifyContent="space-evenly">
                        <Grid item>
                            <Autocomplete
                                onChange={(event, value) => {
                                    setSelectedKey(value);
                                }}
                                sx={{ width: 300 }}
                                disablePortal
                                options={options}
                                value={selectedKey}
                                renderInput={(params) => <TextField {...params} label="Clé" />}
                            />
                        </Grid>
                        <Grid item>
                            <Button onClick={updateOptions} variant="contained" type="submit" sx={{ borderRadius: 28 }}>
                                +
                            </Button>
                        </Grid>
                    </Grid>
                </Paper>
                {selectedKeys.length !== 0 && (
                    <Paper elevation={2} sx={{ p: 2, mt: 2 }}>
                        {selectedKeys.map((key) => (
                            <Typography key={key} sx={{ mt: 2 }} variant="body1">
                                {key} :
                            </Typography>
                        ))}
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