import { useEffect, useState } from "react";
import { Role } from "../../../../conf/userController";
import { Grid, MenuItem, Paper, Select } from "@mui/material";
import { englishRoleToFrench } from "../../../../sharedComponents/NavBar";


function RoleComponent(props: {
    role: Role,
    setRole: React.Dispatch<React.SetStateAction<Role>>
}) {
    const [userRoleList, setUserRoleList] = useState<Role[]>([]);
    const backgroundColor = 'rgb(249, 246, 251)'; // Replace with your desired colors

    useEffect(() => {
        const fetchRoleList = async () => {
            const response = await fetch('/api/user/allRoles');
            const data = await response.json();
            setUserRoleList(data);
        }
        fetchRoleList();
    }, []);

    return (
        <Paper elevation={2} sx={{p: 2, mt: 3, backgroundColor}}>
            <Grid container alignItems="center" justifyContent="space-between">
                <Grid xs={3} item>
                    <h4>Rôle : </h4>
                </Grid>
                <Grid xs={9} item>
                    <Select
                        value={props.role}
                        onChange={event => {
                            let value = event.target.value as Role;
                            props.setRole(value)
                        }}
                        fullWidth={true}>
                        {userRoleList && userRoleList.map((role) => {
                            return (
                            <MenuItem
                                key={"role-"+ role} 
                                value={role}
                            >
                                {englishRoleToFrench(role.toString())}
                            </MenuItem>
                        )})}
                    </Select>
                </Grid>
            </Grid>
        </Paper>
    )
}

export default RoleComponent;