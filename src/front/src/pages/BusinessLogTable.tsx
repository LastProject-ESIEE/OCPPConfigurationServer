import { Box, Grid, ListItemButton, Tooltip, Typography } from "@mui/material";
import React, { useEffect } from "react";
import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "./DisplayTable";
import { BusinessLog, searchBusinessLog } from "../conf/businessLogController";
// import { Link } from "react-router-dom";

const PAGE_SIZE = 30; // Max items displayed in the businessLog table

const businessLogTableColumns: TableColumnDefinition[] = [
    {
        title: "Date",
        /*
        filter: {
          apiField: "containsTitle",
          onChange: value => console.log("Filtering on : " + value)
        }
        */
    },
    {
        title: "User"
    },
    {
        title: "Chargepoint"
    },
    {
        title: "Category",
    },
    {
        title: "Log",
    }
]

export function BusinessLogTable() {
    const [tableData, setTableData] = React.useState<BusinessLog[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    useEffect(() => {
        searchBusinessLog(PAGE_SIZE).then((result: PageRequest<BusinessLog> | undefined) => {
            if (!result) {
                setError("Erreur lors de la récupération des logs métier.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<BusinessLog> = {
        columns: businessLogTableColumns,
        key: "business-log-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        onSelection: businessLog => {
            console.log("Selected item : " + businessLog.id)
        },
        formatter: (businessLog) => {
            return (
                <Box key={"box-configuration-edit-path-" + businessLog.id} paddingTop={1} maxWidth={"true"}>
                    {/*<Link key={"chargepoint-edit-path-" + businessLog.id}  to={{ pathname: 'display/' + businessLog.id}} style={{ textDecoration: 'none', paddingTop: 10 }}>*/}
                    <ListItemButton style={{
                        maxWidth: "true",
                        height: "5vh",
                        padding: 0,
                        paddingTop: 3,
                        borderRadius: 50,
                        color: 'black',
                        backgroundColor: '#E1E1E1'
                    }}>
                        <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                            <Grid item xs={12 / businessLogTableColumns.length} maxWidth={"true"}
                                  justifyContent={"center"}>
                                <Typography variant="body1" align="center"
                                            noWrap={true}>{new Date(businessLog.date).toLocaleString()}</Typography>
                            </Grid>
                            <Grid item xs={12 / businessLogTableColumns.length} maxWidth={"true"}
                                  justifyContent={"center"}>
                                <Tooltip
                                    title={businessLog.user != null && `${businessLog.user.firstName} ${businessLog.user.lastName}`}>
                                    <Typography variant="body1" align="center" noWrap={true}>
                                        {businessLog.user != null &&
                                            `${businessLog.user.firstName} ${businessLog.user.lastName}`}
                                    </Typography>
                                </Tooltip>
                            </Grid>
                            <Grid item xs={12 / businessLogTableColumns.length} maxWidth={"true"}
                                  justifyContent={"center"}>
                                <Tooltip title={businessLog.chargepoint != null && businessLog.chargepoint.clientId}>
                                    <Typography variant="body1" align="center" noWrap={true}>
                                        {businessLog.chargepoint != null && businessLog.chargepoint.clientId}
                                    </Typography>
                                </Tooltip>
                            </Grid>
                            <Grid item xs={12 / businessLogTableColumns.length} maxWidth={"true"}
                                  justifyContent={"center"}>
                                <Typography variant="body1" align="center"
                                            noWrap={true}>{businessLog.category}</Typography>
                            </Grid>
                            <Grid item xs={12 / businessLogTableColumns.length} maxWidth={"true"}
                                  justifyContent={"center"}>
                                <Tooltip title={businessLog.completeLog}>
                                    <Typography variant="body1" align="center"
                                                noWrap={true}>{businessLog.completeLog}</Typography>
                                </Tooltip>
                            </Grid>
                        </Grid>
                    </ListItemButton>
                    {/*</Link>*/}
                </Box>
            )
        },
        fetchData: () => {
            const nextPage = currentPage + 1;
            searchBusinessLog(PAGE_SIZE, nextPage).then((result: PageRequest<BusinessLog> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des logs métier.")
                    return
                }
                setTableData([...tableData, ...result.data])
                setHasMore(result.total > PAGE_SIZE * (nextPage + 1))
            });
            setCurrentPage(nextPage)
        },
    }

    return InfinityScrollItemsTable(props)
}