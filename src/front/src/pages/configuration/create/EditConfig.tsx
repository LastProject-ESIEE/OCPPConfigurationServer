import { Box, Typography } from "@mui/material";
import { useParams } from "react-router-dom";
import React from "react";
import CreateConfig from "./CreateConfig";

export function ConfigurationEditPage() {
    // Get id from router parameter
    const { id } = useParams();
    const [error, setError] = React.useState<string | undefined>(undefined);
    
    setError(undefined) // bypasss eslint, error must be managed

    let configurationId = id ? Number(id) : undefined;
    return (
        <Box maxWidth={"true"}>
            {error && (
                <Box>
                    <Typography variant="h5" color={"red"}>Erreur lors de la récupération de la configuration: {error}</Typography>
                </Box>
            )}
            {!error && (
                <Box>
                    <CreateConfig id={configurationId}/>
                </Box>
            )}
        </Box>
    );
}

