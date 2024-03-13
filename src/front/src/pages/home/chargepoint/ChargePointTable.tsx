import { Box, Grid, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import {
    DEFAULT_FILTER_SELECT_VALUE,
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { ChargePoint, WebSocketChargePointNotification } from "../../../conf/chargePointController";
import { Link } from "react-router-dom";
import { notificationManager } from "../Home";
import { searchElements } from "../../../conf/backendController";

const PAGE_SIZE = 30; // Max items displayed in the chargepoint table

// Table columns definition 
const chargePointTableColumns: TableColumnDefinition[] = [
  {
    title: "Identifiant client",
    size: 2,
    filter: {
      apiField: "clientId",
      filterType: "input"
    },
    sort: {
        apiField: "clientId",
    }
  },
  {
    title: "Constructeur",
    size: 2,
    filter: {
      apiField: "constructor",
      filterType: "input"
    },
    sort: {
        apiField: "constructor",
    }
  },
  {
    title: "Modèle",
    size: 2,
    filter: {
      apiField: "type",
      filterType: "input"
    },
    sort: {
        apiField: "type",
    }
  },
  {
    title: "Étape",
    size: 2,
    filter: {
      apiField: "step",
      filterType: "select",
      restrictedValues: [
        DEFAULT_FILTER_SELECT_VALUE,
        "FIRMWARE",
        "CONFIGURATION",
      ],
      apiValueFormatter: value => {
          return value === DEFAULT_FILTER_SELECT_VALUE ? "" : value
      },
    },
    sort: {
        apiField: "step",
    }
  },
  {
    title: "Status",
    size: 2,
    filter: {
      apiField: "status",
      filterType: "select",
      restrictedValues: [
        DEFAULT_FILTER_SELECT_VALUE,
        "PENDING",
        "PROCESSING",
        "FINISHED",
        "FAILED",
      ],
      apiValueFormatter: value => {
          return value === DEFAULT_FILTER_SELECT_VALUE ? "" : value
      },
    },
    sort: {
        apiField: "status",
    }
  },
  {
    title: "Dernière activité",
    size: 2,
    filter: {
      apiField: "lastUpdate",
      filterType: "date"
    },
    sort: {
        apiField: "lastUpdate",
    }
  }
]

/**
 * Display chargepoint table
 * 
 * @returns 
 */
function ChargePointTable() {
    const [tableData, setTableData] = React.useState<ChargePoint[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);
    const [total, setTotal] = React.useState<number>();
    const [totalElement, setTotalElement] = React.useState<number>();
    const [loaded, setLoaded] = React.useState(false);

    // Add event listener on the websocket connection
    useEffect(() => {
      let callBack =  (message: WebSocketChargePointNotification) => {
         let chargePoint = tableData.find(p => p.id === message.id)
         if (!chargePoint) {
          return
        }
        // Update charge point status
        chargePoint.status = Object.assign({}, message.status)
        setTableData([...tableData]) 
      }
      notificationManager.addListener('charge-point-update', callBack)
      return () => {
        notificationManager.removeListener('charge-point-update', callBack)
      };
    }, [tableData])

    // Fetch first charge point page on component load
    useEffect(() => {
      searchElements<ChargePoint>("/api/chargepoint/search",
          {
              page: 0,
              size: PAGE_SIZE,
              sort: { field: "lastUpdate", order: "desc" }
          }).then((result: PageRequest<ChargePoint> | undefined) => {
        if (!result) {
          setError("Erreur lors de la récupération des bornes.")
          return
        }
        setTableData(result.data)
        setHasMore(result.total > PAGE_SIZE)
        setTotal(result.total)
        setTotalElement(result.totalElement)
        setLoaded(true)
      });
    }, [])

    let props: InfinityScrollItemsTableProps<ChargePoint> = {
      columns: chargePointTableColumns,
      key: "charge-point-table",
      data: tableData,
      hasMore: hasMore,
      error: error,
      formatter: (chargePoint) => {
        let color = chargePoint.status.state ? 'rgba(0, 255, 0, 0.5)' : 'rgba(255, 0, 0, 0.5)'
        return (
          <Box key={"box-chargepoint-edit-path-" + chargePoint.id}  paddingTop={1} maxWidth={"true"}>
              <Link key={"chargepoint-edit-path-" + chargePoint.id}  to={{ pathname: 'edit/' + chargePoint.id}} style={{ textDecoration: 'none', paddingTop: 10 }}>
                  <ListItemButton style={{maxWidth: "true", height:"5vh", padding: 0, paddingTop: 3, borderRadius: 50, color: 'black', backgroundColor: color}}>
                      <Grid container maxWidth={"true"} flexDirection={"row"} alignItems={"center"}>
                          <Grid item xs={chargePointTableColumns[0].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{chargePoint.clientId}</Typography>
                          </Grid>
                          <Grid item xs={chargePointTableColumns[0].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{chargePoint.constructor}</Typography>
                          </Grid>
                          <Grid item xs={chargePointTableColumns[2].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{chargePoint.type}</Typography>
                          </Grid>
                          <Grid item xs={chargePointTableColumns[3].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{chargePoint.status.step}</Typography>
                          </Grid>
                          <Grid item xs={chargePointTableColumns[4].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{chargePoint.status.status}</Typography>
                          </Grid>
                          <Grid item xs={chargePointTableColumns[5].size} maxWidth={"true"} justifyContent={"center"}>
                              <Typography variant="body1" align="center" noWrap={true}>{new Date(chargePoint.status.lastUpdate).toLocaleString()}</Typography>
                          </Grid>
                      </Grid>
                  </ListItemButton>
              </Link>
          </Box>
        )
       },
      fetchData: (filters, sort) => {
        const nextPage = currentPage + 1;
        searchElements<ChargePoint>("/api/chargepoint/search", {page: nextPage, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<ChargePoint> | undefined) => {
          if (!result) {
            setError("Erreur lors de la récupération des bornes.")
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
        searchElements<ChargePoint>("/api/chargepoint/search", {page: 0, size: PAGE_SIZE, filters: filters, sort: sort}).then((result: PageRequest<ChargePoint> | undefined) => {
            if (!result) {
              setError("Erreur lors de la récupération des bornes.")
              return
            }
            setTableData(result.data)
            setHasMore(result.total > PAGE_SIZE)
            setTotal(result.total)
            setTotalElement(result.totalElement)
        });
      },
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

export default ChargePointTable;