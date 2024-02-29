import { Box, Typography } from "@mui/material";
import { useEffect } from "react";
import { useParams } from "react-router-dom";
import { getConfiguration } from "../../../conf/configurationController";
import React from "react";

export function ConfigurationEditPage() {
    // Get id from router parameter
    const { paramId } = useParams();
    const [error, setError] = React.useState<string | undefined>(undefined);

    let configurationId = paramId ? Number(paramId) : undefined;

    // Fetch the configuration
    useEffect(() => {
        if(!configurationId){
            setError("Identifiant de la configuration non valide.")
            return
        }
        getConfiguration(configurationId).then(result => {
            if(!result){
            setError("Erreur lors de la récupération de la configuration.")
            return
            }
        });
        }, [])

    return (
        <Box maxWidth={"true"}>
            {error && (
                <Box>
                    <Typography variant="h5" color={"red"}>Erreur lors de la récupération de la configuration: {error}</Typography>
                </Box>
            )}
            
        </Box>
    );
}

