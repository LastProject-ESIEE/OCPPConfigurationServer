import { Button, Grid } from "@mui/material";
import FormInput from "../../../sharedComponents/FormInput";
import RoleComponent from "./components/RoleComponent";
import { useState } from "react";
import { Role, createNewUser } from "../../../conf/userController";


function AddAccount() {
    const [lastName, setLastName] = useState("");
    const [firstName, setFirstName] = useState("");
    const [mail, setMail] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState<Role>(Role.VISUALIZER);

    return (
        <Grid container spacing={5} maxWidth="xl" sx={{ml: 4, mr: 4}}>
            <Grid item xs={12}>
                <FormInput name={"Nom"}
                    onChange={lastName => setLastName(lastName)}
                />
            </Grid>
            <Grid item xs={12}>
                <FormInput name={"Prénom"}
                    onChange={firstName => setFirstName(firstName)}
                />
            </Grid>
            <Grid item xs={12}>
                <FormInput name={"Email"}
                    onChange={mail => setMail(mail)}
                />
            </Grid>
            <Grid item xs={12}>
                <FormInput name={"Mot de passe"}
                    onChange={password => setPassword(password)}
                />
            </Grid>
            <Grid item xs={12}>
                <RoleComponent role={role} setRole={setRole}/>
            </Grid>
            <Grid item xs={12}>
            <Button 
                sx={{borderRadius: 28}} 
                variant="contained" 
                color="primary"
                onClick={() => {
                        const user = {
                            firstName: firstName,
                            lastName: lastName, 
                            email: mail, 
                            password: password, 
                            role: role
                        }
                        if (user !== undefined) {
                            createNewUser(user)
                        }
                    }
                }
            >
                Créer
            </Button>
            </Grid>
        </Grid>
    )
}

export default AddAccount;