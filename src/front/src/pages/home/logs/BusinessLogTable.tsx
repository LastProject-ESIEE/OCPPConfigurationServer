import { Box, Grid, Tooltip, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import {
    DEFAULT_FILTER_SELECT_VALUE,
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { BusinessLog } from "../../../conf/businessLogController";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
import { searchElements } from "../../../conf/backendController";

const PAGE_SIZE = 30; // Max items displayed in the businessLog table

const businessLogTableColumns: TableColumnDefinition[] = [
    {
        title: "Date",
        size: 2,
        filter: {
            apiField: "date",
            filterType: "date"
        },
        sort: {
            apiField: "date",
        }
    },
    {
        title: "Criticité",
        size: 1,
        filter: {
            apiField: "level",
            filterType: "select",
            restrictedValues: [
                DEFAULT_FILTER_SELECT_VALUE,
                "FATAL",
                "ERROR",
                "WARN",
                "INFO",
                "DEBUG",
                "TRACE",
              ],
            apiValueFormatter: value => {
                  return value === DEFAULT_FILTER_SELECT_VALUE ? "" : value
              },
        },
        sort: {
            apiField: "level",
        }
    },
    {
        title: "Catégorie",
        size: 1,
        filter: {
            apiField: "category",
            filterType: "select",
            restrictedValues: [
                DEFAULT_FILTER_SELECT_VALUE,
                "LOGIN",
                "STATUS",
                "FIRM",
                "CONFIG",
              ],
            apiValueFormatter: value => {
                  return value === DEFAULT_FILTER_SELECT_VALUE ? "" : value
              },
        },
        sort: {
            apiField: "category",
        }
    },
    {
        title: "Utilisateur",
        size: 1,
        filter: {
            apiField: "",
            filterType: "input",
            disable: true,
        }
    },
    {
        title: "Borne",
        size: 2,
        filter: {
            apiField: "",
            filterType: "input",
            disable: true,
        }
    },
    {
        title: "Log",
        size: 4.5,
        filter: {
            apiField: "completeLog",
            filterType: "input"
        },
        sort: {
            apiField: "completeLog",
        }
    },
    {
        title: "",
        size: 0.5,
    }
]

function BusinessLogTable() {
    const [tableData, setTableData] = React.useState<BusinessLog[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);
    const [total, setTotal] = React.useState<number>();
    const [totalElement, setTotalElement] = React.useState<number>();
    const [loaded, setLoaded] = React.useState(false);

    useEffect(() => {
        searchElements<BusinessLog>("/api/log/business/search",
            {
                page: 0,
                size: PAGE_SIZE,
                sort: {field: 'date', order: "desc"}
            }).then((result: PageRequest<BusinessLog> | undefined) => {
            if (!result) {
                setError("Erreur lors de la récupération des logs métier.")
                return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
            setTotal(result.total)
            setTotalElement(result.totalElement)
            setLoaded(true)
        });
    }, [])


    let props: InfinityScrollItemsTableProps<BusinessLog> = {
        columns: businessLogTableColumns,
        key: "business-log-table",
        data: tableData,
        hasMore: hasMore,
        error: error,
        formatter: (businessLog, index) => {
            return (
                <Box key={"box-businesslog-" + index} paddingTop={1} maxWidth={"true"}>
                    <LogLineItemVMamar businessLog={businessLog}/>
                </Box>
            )
        },
        fetchData: (filters, sort) => {
            const nextPage = currentPage + 1;
            searchElements<BusinessLog>("/api/log/business/search", {
                page: nextPage,
                size: PAGE_SIZE,
                filters: filters,
                sort: sort
            }).then((result: PageRequest<BusinessLog> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des logs métier.")
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
            searchElements<BusinessLog>("/api/log/business/search", {
                page: 0,
                size: PAGE_SIZE,
                filters: filters,
                sort: sort
            }).then((result: PageRequest<BusinessLog> | undefined) => {
                if (!result) {
                    setError("Erreur lors de la récupération des logs métier.")
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

function LogLineItemVMamar(props: { businessLog: BusinessLog }) {
    const [open, setOpen] = useState(false);

    return (
        <Box style={{
            paddingTop: 3,
            paddingBottom: 3,
            borderRadius: open ? 25 : 50,
            backgroundColor: '#E1E1E1'
        }}>
            <Grid container maxWidth={"true"}
                  flexDirection={"row"}
                  alignItems={"center"}
                  onClick={() => setOpen(!open)}>
                <Grid item xs={businessLogTableColumns[0].size}
                      maxWidth={"true"}
                      justifyContent={"center"}>
                    <Typography variant="body1"
                                align="center"
                                noWrap={true}>
                        {new Date(props.businessLog.date).toLocaleString()}</Typography>
                </Grid>
                <Grid item xs={businessLogTableColumns[1].size}
                      maxWidth={"true"}
                      justifyContent={"center"}>
                    <Typography variant="body1"
                                align="center"
                                noWrap={true}>
                        {props.businessLog.level}</Typography>
                </Grid>
                <Grid item xs={businessLogTableColumns[2].size} maxWidth={"true"}
                      justifyContent={"center"}>
                    <Typography variant="body1" align="center"
                                noWrap={true}>{props.businessLog.category}</Typography>
                </Grid>
                <Grid item xs={businessLogTableColumns[3].size}
                      maxWidth={"true"}
                      justifyContent={"center"}>
                    <Tooltip
                        title={props.businessLog.user != null && `${props.businessLog.user.firstName} ${props.businessLog.user.lastName}`}>
                        <Typography variant="body1" align="center" noWrap={true}>
                            {props.businessLog.user != null &&
                                `${props.businessLog.user.firstName} ${props.businessLog.user.lastName}`}
                        </Typography>
                    </Tooltip>
                </Grid>
                <Grid item xs={businessLogTableColumns[4].size} maxWidth={"true"}
                      justifyContent={"center"}>
                    <Tooltip title={props.businessLog.chargepoint != null && props.businessLog.chargepoint.clientId}>
                        <Typography variant="body1" align="center" noWrap={true}>
                            {props.businessLog.chargepoint != null && props.businessLog.chargepoint.clientId}
                        </Typography>
                    </Tooltip>
                </Grid>
                <Grid item xs={businessLogTableColumns[5].size} maxWidth={"true"}
                      justifyContent={"center"}>
                    {!open
                        ? <Tooltip title={props.businessLog.completeLog}>
                            <Typography variant="body1"
                                        align="center"
                                        noWrap={true}>
                                {props.businessLog.completeLog}
                            </Typography>
                        </Tooltip>
                        : <Typography variant="inherit"
                                      align="center"
                                      noWrap={true}
                                      color={'rgb(130,130,130)'}>
                            {props.businessLog.completeLog}
                        </Typography>}
                </Grid>
                <Grid item xs={businessLogTableColumns[6].size}>
                    <Typography align={"center"}>
                        {!open
                            ? <ArrowDropDownIcon/>
                            : <ArrowDropUpIcon/>}
                    </Typography>
                </Grid>
            </Grid>
            {open && (
                <Typography align={"center"}
                            onClick={undefined}
                            marginLeft={"10vh"}
                            marginRight={"10vh"}
                            paddingTop={1}
                            paddingBottom={1}>
                    {props.businessLog.completeLog}</Typography>
            )}
        </Box>
    )
}

export default BusinessLogTable;