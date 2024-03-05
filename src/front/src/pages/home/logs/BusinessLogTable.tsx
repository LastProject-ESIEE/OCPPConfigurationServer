import { Box, Grid, ListItemButton, Tooltip, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { BusinessLog, searchBusinessLog } from "../../../conf/businessLogController";
import { TechnicalLog } from "../../../conf/technicalLogController";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import ArrowDropUpIcon from "@mui/icons-material/ArrowDropUp";
// import { Link } from "react-router-dom";

const PAGE_SIZE = 30; // Max items displayed in the businessLog table

const businessLogTableColumns: TableColumnDefinition[] = [
    {
        title: "Date",
        size:11.5/5,
        /*
        filter: {
          apiField: "containsTitle",
          onChange: value => console.log("Filtering on : " + value)
        }
        */
    },
    {
        title: "User",
        size:11.5/5,
    },
    {
        title: "Chargepoint",
        size:11.5/5
    },
    {
        title: "Category",
        size:11.5/5,
    },
    {
        title: "Log",
        size:11.5/5,
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
                    <LogLineItemVMamar businessLog={businessLog}/>
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

function LogLineItemVMamar(props: { businessLog: BusinessLog }) {
    const [open, setOpen] = useState(false);

    return (
        <Box onClick={() => setOpen(!open)}
             style={{
                 paddingTop: 3,
                 paddingBottom: 3,
                 borderRadius: open ? 25 : 50,
                 backgroundColor: '#E1E1E1'
             }}>
            <Grid container maxWidth={"true"}
                  flexDirection={"row"}
                  alignItems={"center"}>
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
                    <Tooltip
                        title={props.businessLog.user != null && `${props.businessLog.user.firstName} ${props.businessLog.user.lastName}`}>
                        <Typography variant="body1" align="center" noWrap={true}>
                            {props.businessLog.user != null &&
                                `${props.businessLog.user.firstName} ${props.businessLog.user.lastName}`}
                        </Typography>
                    </Tooltip>
                </Grid>
                <Grid item xs={businessLogTableColumns[2].size} maxWidth={"true"}
                      justifyContent={"center"}>
                    <Tooltip title={props.businessLog.chargepoint != null && props.businessLog.chargepoint.clientId}>
                        <Typography variant="body1" align="center" noWrap={true}>
                            {props.businessLog.chargepoint != null && props.businessLog.chargepoint.clientId}
                        </Typography>
                    </Tooltip>
                </Grid>
                <Grid item xs={businessLogTableColumns[3].size} maxWidth={"true"}
                      justifyContent={"center"}>
                    <Typography variant="body1" align="center"
                                noWrap={true}>{props.businessLog.category}</Typography>
                </Grid>
                <Grid item xs={businessLogTableColumns[4].size} maxWidth={"true"}
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
                <Grid item xs={businessLogTableColumns[5].size}>
                    <Typography align={"center"}>
                        {!open
                            ? <ArrowDropDownIcon/>
                            : <ArrowDropUpIcon/>}
                    </Typography>
                </Grid>
            </Grid>
            {open && (
                <Typography align={"center"}
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