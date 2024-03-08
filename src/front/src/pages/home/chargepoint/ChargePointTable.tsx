import { Box, Grid, ListItemButton, Typography } from "@mui/material";
import React, { useEffect } from "react";
import {
    InfinityScrollItemsTable,
    InfinityScrollItemsTableProps,
    PageRequest,
    TableColumnDefinition
} from "../../../sharedComponents/DisplayTable";
import { ChargePoint, searchChargePoint, WebSocketChargePointNotification } from "../../../conf/chargePointController";
import { Link } from "react-router-dom";
import { wsManager } from "../Home";

const PAGE_SIZE = 30; // Max items displayed in the chargepoint table

const chargePointTableColumns: TableColumnDefinition[] = [
  {
    title: "Identifiant client",
    /*
    filter: {
      apiField: "containsTitle",
      onChange: value => console.log("Filtering on : " + value)
    }
    */
    size: 2
  },
  {
    title: "Constructeur",
    size: 2
  },
  {
    title: "Modèle",
    size: 2
  },
  {
    title: "Étape",
    size: 2 
  },
  {
    title: "Status",
    size: 2
  },
  {
    title: "Dernière activité",
    size: 2
  }
]

function ChargePointTable() {
    const [tableData, setTableData] = React.useState<ChargePoint[]>([]);
    const [currentPage, setCurrentPage] = React.useState(0);
    const [hasMore, setHasMore] = React.useState(true);
    const [error, setError] = React.useState<string | undefined>(undefined);

    // Add event listener on the websocket connection
    useEffect(() => {
      let callBack =  (message: WebSocketChargePointNotification) => {
         let chargePoint = tableData.find(p => p.id === message.id)
         if(!chargePoint){
          return
        }
        // Update charge point status
        chargePoint.status = Object.assign({}, message.status)
        setTableData([...tableData]) 
      }
      wsManager.addListener('charge-point-update', callBack)
      return () => {
        wsManager.removeListener('charge-point-update', callBack)
      };
    }, [tableData])

    // Fetch first charge point page on component load
    useEffect(() => {
      searchChargePoint(PAGE_SIZE).then((result: PageRequest<ChargePoint> | undefined) => {
        if(!result){
          setError("Erreur lors de la récupération des bornes.")
          return
        }
        setTableData(result.data)
        setHasMore(result.total > PAGE_SIZE)
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
          <Box key={"box-configuration-edit-path-" + chargePoint.id}  paddingTop={1} maxWidth={"true"}>
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
      fetchData: () => {
        const nextPage = currentPage + 1;
        searchChargePoint(PAGE_SIZE,nextPage).then((result: PageRequest<ChargePoint> | undefined) => {
          if(!result){
            setError("Erreur lors de la récupération des bornes.")
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

export default ChargePointTable;