import {useEffect, useState} from "react";
import {ApiRole, apiRoleToFrench} from "../../../../conf/userController";
import {Grid, MenuItem, Paper, Select, Typography} from "@mui/material";

/**
 * Editable role componant
 * @param props The component props
 */
function RoleComponent(props: {
    role: ApiRole,
    setRole: React.Dispatch<React.SetStateAction<ApiRole>>
}) {
    const [userRoleList, setUserRoleList] = useState<ApiRole[]>([]);
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
                    <Typography variant={"h6"}>Rôle : </Typography>
                </Grid>
                <Grid xs={9} item>
                    <Select
                        value={props.role}
                        onChange={event => {
                            let value = event.target.value as ApiRole;
                            props.setRole(value)
                        }}
                        fullWidth={true}>
                        {userRoleList && userRoleList.map((role) => {
                            return (
                            <MenuItem
                                key={"role-"+ role}
                                value={role}
                                disabled={role === props.role}
                            >
                                {apiRoleToFrench(role)}
                            </MenuItem>
                        )})}
                    </Select>
                </Grid>
            </Grid>
        </Paper>
    )
}

export default RoleComponent;