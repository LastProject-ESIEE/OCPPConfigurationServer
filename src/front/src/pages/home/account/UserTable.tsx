import { Box, Grid, MenuItem, Select, SelectChangeEvent, Typography } from "@mui/material";
import React, { useEffect } from "react";
import {
    DEFAULT_FILTER_SELECT_VALUE,
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { ApiRole, apiRoleToFrench, FrenchRole, frenchToEnglishRole, User, UserInformation } from "../../../conf/userController";
import DeleteUserModalComponent from "./components/DeleteUserModalComponent";
import { getUserInformation, searchElements } from "../../../conf/backendController";

const PAGE_SIZE = 30; // Max items displayed in the user table

// Table columns defintion
const userTableColumns: TableColumnDefinition[] = [
    {
        title: "Nom",
        size: 2.5,
        filter: {
            apiField: "lastName",
            filterType: "input"
        },
        sort: {
            apiField: "lastName",
        }
    },
    {
        title: "Prénom",
        size: 2.5,
        filter: {
            apiField: "firstName",
            filterType: "input"
        },
        sort: {
            apiField: "firstName",
        }
    },
    {
        title: "Mail",
        size: 3.5,
        filter: {
            apiField: "email",
            filterType: "input"
        },
        sort: {
            apiField: "email",
        }
    },
    {
        title: "Rôle",
        filter: {
            apiField: "role",
            filterType: "select",
            restrictedValues: [
                DEFAULT_FILTER_SELECT_VALUE,
                apiRoleToFrench("ADMINISTRATOR"),
                apiRoleToFrench("EDITOR"),
                apiRoleToFrench("VISUALIZER"),
            ],
            apiValueFormatter: value => {
                return value === DEFAULT_FILTER_SELECT_VALUE ? "" : frenchToEnglishRole(value as FrenchRole)
            }
        },
        size: 3,
        sort: {
            apiField: "role",
        }
    },
    {
        title: "",
        size: 0.5
    }
]

/**
 * Display users table
 */
function UserTable() {
    const [tableData, setTableData] = React.useState<User[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);
    const [userRoleList, setUserRoleList] = React.useState<ApiRole[]>([]);
    const [me, setMe] = React.useState<UserInformation | undefined>(undefined);
    const [total, setTotal] = React.useState<number>();
    const [totalElement, setTotalElement] = React.useState<number>();
    const [loaded, setLoaded] = React.useState(false);

    // Fetch first page of users
    useEffect(() => {
        searchElements<User>("/api/user/search", {
            page: 0,
            size: PAGE_SIZE,
            sort: {field: 'lastName', order: "asc"}
        }).then((result: PageRequest<User> | undefined) => {
            if (!result) {
                setError("Erreur lors de la récupération des utilisateurs.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
            setTotal(result.total)
            setTotalElement(result.totalElement)
            setLoaded(true)
        });
    }, [])

    // Fetch user role list
    useEffect(() => {
        const fetchRoleList = async () => {
            const response = await fetch('/api/user/allRoles');
            const data = await response.json();
            setUserRoleList(data);
        }
        fetchRoleList();
    }, []);

    // Retrieve user's information
    useEffect(() => {
        getUserInformation().then(userInfo => {
            if (!userInfo) {
                console.error("Failed to retrieve user information.")
                return
            }
            setMe(userInfo)
        })
    }, []);

    // Update user role on input change 
    function onChangeEvent(event: SelectChangeEvent<ApiRole>, user: User) {
        let role = event.target.value as ApiRole
        fetch(`/api/user/${user.id}/role/${role}`, {
            method: "PATCH",
        }).then(response => {
            if (response.ok) {
                // TODO : refactor to use the DTO in the response
                if (!user) {
                    return;
                }
                let updatedUser = tableData.find(u => u.id === user.id)
                if (!updatedUser) {
                    return
                }
                // Update role of user
                user.role = role
                setTableData([...tableData])
            }
        })
    }

    let props: InfinityScrollItemsTableProps<User> = {
        columns: userTableColumns,
        key: "user-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        formatter: (user, index) => {
            return (
                <Box key={"box-user-table-entry-" + index} margin={1} maxWidth={"true"}>
                    <Box style={{
                        maxWidth: "true",
                        margin: 3,
                        borderRadius: 50,
                        color: 'black',
                        backgroundColor: '#E1E1E1'
                    }}>
                        <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                            <Grid item xs={userTableColumns[0].size} maxWidth={"true"} justifyContent={"center"}>
                                <Typography variant="body1" align="center">{user.lastName}</Typography>
                            </Grid>
                            <Grid item xs={userTableColumns[1].size} maxWidth={"true"} justifyContent={"center"}>
                                <Typography variant="body1" align="center">{user.firstName}</Typography>
                            </Grid>
                            <Grid item xs={userTableColumns[2].size} maxWidth={"true"} justifyContent={"center"}>
                                <Typography variant="body1" align="center">{user.email}</Typography>
                            </Grid>
                            <Grid item xs={userTableColumns[3].size} maxWidth={"true"} justifyContent={"center"}>
                                <Select
                                    disabled={user.id === me?.id}
                                    value={user.role}
                                    style={{
                                        border: 0,
                                        textAlign: "center",
                                        marginTop: 10,
                                        marginBottom: 10
                                    }}
                                    onChange={event => {
                                        onChangeEvent(event, user)
                                    }}
                                    fullWidth={true} size="small" variant="standard">

                                    {userRoleList && userRoleList
                                        .map(role => {
                                                return (
                                                    <MenuItem
                                                        key={"menuItem" + role.toString()}
                                                        value={role}
                                                        disabled={role === user.role}
                                                        style={{
                                                            border: 0
                                                        }}
                                                    >
                                                        {apiRoleToFrench(role)}
                                                    </MenuItem>
                                                )
                                            }
                                        )}
                                </Select>
                            </Grid>
                            <Grid item xs={userTableColumns[4].size} maxWidth={"true"} justifyContent={"center"}
                                  textAlign={"center"}>
                                <DeleteUserModalComponent
                                    user={user}
                                    enabled={user.id === me?.id}
                                    setTableData={setTableData}
                                    setError={setError}
                                    setHasMore={setHasMore}
                                />
                            </Grid>
                        </Grid>
                    </Box>
                </Box>

            )
        },
        fetchData: (filters, sort) => {
            const nextPage = currentPage + 1;
            searchElements<User>("/api/user/search", {
                page: nextPage,
                size: PAGE_SIZE,
                filters: filters,
                sort: sort
            }).then((result: PageRequest<User> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des utilisateurs.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
                setTotal(result.total)
                setTotalElement(result.totalElement)
            });
            setCurrentPage(nextPage)
        },
        onFiltering: (filters, sort) => {
            // Reset page and search
            setCurrentPage(0)
            searchElements<User>("/api/user/search", {
                page: 0,
                size: PAGE_SIZE,
                filters: filters,
                sort: sort
            }).then((result: PageRequest<User> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des utilisateurs.")
                    return
                }
                setTableData(result.data)
                setHasMore(result.total > PAGE_SIZE)
                setTotal(result.total)
                setTotalElement(result.totalElement)
            });
        }
    }

    return (
        <Box>
            {
                InfinityScrollItemsTable(props)
            }
            {
                loaded && (
                    <Typography
                        color={"primary"}
                        variant="body1"
                        marginLeft={5}
                        >
                        {
                            (total === totalElement) ? totalElement?.toLocaleString() + " élément(s)" : total?.toLocaleString() + " élément(s) sur " + totalElement?.toLocaleString()
                        }
                    </Typography>
                )
            }
        </Box>
    )
}

export default UserTable;
