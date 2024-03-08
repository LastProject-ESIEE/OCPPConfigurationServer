import { Alert, Box, Button, Drawer, Grid, List, ListItem, ListItemText, Snackbar, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { wsManager } from "../pages/home/Home";
import { SnackbarProvider, useSnackbar } from "notistack";
import React from "react";
import MailIcon from '@mui/icons-material/Mail';
/*
function DisplayNotification(){
    const [open, setOpen] = useState(false);
    const [value, setValue] = useState("");
    const { enqueueSnackbar } = useSnackbar();

    const handleClick = () => {
        setOpen(!open)
    }

    const handleClose = () => {
        setOpen(!open)
    }

    // Add event listener on the websocket connection
    useEffect(() => {
        let callBack =  (message: string) => {  
            console.log("NOTIFFFIIER")
            setOpen(true)
            enqueueSnackbar(message, {variant: "info"})
            //setValue(message)
        }
        wsManager.addListener('notify', callBack)
        return () => {
          wsManager.removeListener('notify', callBack)
        };
      }, [])
  

    return (
        <SnackbarProvider maxSnack={3}>
                
        </SnackbarProvider>
    )
}
*/

const LOCAL_STORAGE_NOTIFICATION_KEY = "NOTIFICATION_MESSAGES"
const LOCAL_STORAGE_MAX_NOTIFICATION_COUNT = 1000
const LOCAL_STORAGE_CLEAR_OFFSET = 300

export type NotificationType = "ERROR" | "INFO"

export type NotificationMessage = {
    title: string,
    type: NotificationType
    content: string
}

export default function DisplayNotification(props: {open: boolean, onClose: () => void}) {
    const [notificationList, setNotificationlist] = useState<NotificationMessage[]>([]);
    
    useEffect(() => {
        // load notification from cache
        let notificationsCache = localStorage.getItem(LOCAL_STORAGE_NOTIFICATION_KEY)
        if(notificationsCache){
            let notifications = JSON.parse(notificationsCache)
            if(notifications){
                setNotificationlist(notifications)
            }
        }
    }, [])
    
    // Save notification in cache
    const saveNotification = (message: NotificationMessage) => {
        let newList = [message, ...notificationList]
        if(notificationList.length > LOCAL_STORAGE_MAX_NOTIFICATION_COUNT){
            let reducedList = newList.slice(0, LOCAL_STORAGE_MAX_NOTIFICATION_COUNT - LOCAL_STORAGE_CLEAR_OFFSET)
            localStorage.setItem(LOCAL_STORAGE_NOTIFICATION_KEY, JSON.stringify(reducedList))
            setNotificationlist(reducedList)
            return
        }
        localStorage.setItem(LOCAL_STORAGE_NOTIFICATION_KEY, JSON.stringify(newList))
        setNotificationlist(newList)
    }

    return (
    <Box>
        <Drawer
            anchor={'right'}
            open={props.open}
            onClose={() => props.onClose()}
            style={{margin: 5, overflow: "hidden"}}
        >
            <Box padding={1}>
                <Box>
                    <Grid >

                    </Grid>
                    <Typography variant="h5">Historique :</Typography>
                </Box>
                <Box height={"90vh"} overflow={"auto"} marginTop={2}>
                    <List style={{overflow: "auto"}}>
                        {notificationList.map(notif => {
                            return (
                                <ListItem key={"notif-list-item-" + notif.title}>
                                    <ListItemText title={notif.title} content={notif.content} style={{width: 400}}>
                                        <Grid container flexDirection={"column"}>
                                            <Grid item xs={12}>
                                                <Typography variant="h6">{notif.title}</Typography>
                                            </Grid>
                                            <Grid item xs={12}>
                                                <Typography variant="body1">{notif.content}</Typography>
                                            </Grid>
                                        </Grid>
                                    </ListItemText>
                                </ListItem>
                            )
                        })}
                    </List>
                </Box>
            </Box>
        </Drawer>
        <SnackbarProvider maxSnack={3}>
            <NotificationItems onNotificationReceived={message => {
                saveNotification(message)
            }}/>
        </SnackbarProvider>
    </Box>
    );
}

function NotificationItems(props: {onNotificationReceived: (message: NotificationMessage) => void}) {
    const { enqueueSnackbar } = useSnackbar();
  
    // Add event listener on the websocket connection
    useEffect(() => {
        let callBack =  (message: NotificationMessage) => {  
            enqueueSnackbar(message.title + "\n" + message.content, {variant: message.type === "ERROR" ? "error" : "info"})
            props.onNotificationReceived(message)
        }
        wsManager.addListener('notify', callBack)
        return () => {
          wsManager.removeListener('notify', callBack)
        };
      }, [props])
  
  
    return (
        <React.Fragment></React.Fragment>
    );
  }
  

/*
           <Snackbar open={open} autoHideDuration={5000} onClose={handleClose}>
                <Alert
                    onClose={handleClose}
                    severity="success"
                    variant="filled"
                    sx={{ width: '100%' }}
                >
                {value}
                </Alert>
            </Snackbar>
*/