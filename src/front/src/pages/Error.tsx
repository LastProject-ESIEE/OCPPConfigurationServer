import * as React from 'react';
import {Grid} from "@mui/material";
import Box from "@mui/material/Box";

function Error() {

    return (
        <Box>
            <Grid alignContent={"center"}>
                <Grid xs={12}>
                    <Box textAlign={"center"}>
                        <img src="assets/logo-brs.png" alt="logo de BRS"/>
                    </Box>
                </Grid>
                <Grid xs={12}>
                    <Box textAlign={"center"}>
                        Bienvenue !
                    </Box>
                </Grid>
                <Grid>
                    <Box textAlign={"center"}>
                        Tu sembles t'être perdu, non ?
                    </Box>
                </Grid>
                <Grid>
                    <Box textAlign={"center"}>
                        Regarde ici : <a href="https://www.youtube.com/watch?v=dQw4w9WgXcQ&ab_channel=RickAstley">Accueil</a>
                    </Box>
                </Grid>
            </Grid>
        </Box>
    );
}

export default Error;
