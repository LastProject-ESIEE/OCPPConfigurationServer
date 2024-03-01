import React, { Dispatch, SetStateAction } from "react";
import { Button, Grid, Input } from "@mui/material";
import Typography from "@mui/material/Typography";
import { GlobalState, Transcriptor } from "../../../conf/configurationController";

function KeyValuePair(props: {
    selectedKey: Transcriptor,
    value: string,
    globalState: GlobalState,
    setGlobalState: Dispatch<SetStateAction<GlobalState>>,
    setSelectedKeys: React.Dispatch<React.SetStateAction<Transcriptor[]>>,
    selectedKeys: Transcriptor[]
}): JSX.Element {
    const {
        selectedKey,
        setGlobalState,
        setSelectedKeys,
        selectedKeys,
    } = props;

    //const [currentValue, setCurrentValue] = useState(props.value);

    function handleChange(event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const newValue = event.target.value
        //setCurrentValue(newValue)
        /*
        const newKey: KeyValueConfiguration = {
            key: selectedKey,
            value: newValue
        }
        */
        setGlobalState(prevState => {
            let updated = false;
            console.log(prevState.configuration)
            let itemValue = prevState.configuration.find(conf => conf.key.id === selectedKey.id)
            if(itemValue){
                itemValue.value = newValue
                updated = true
            }
            /*
            for(let key in prevState.configuration){
                console.log(key)
                if (Number(key) === newKey.key.id) {
                    let itemValue = prevState.configuration.find(v => v.key.id == newKey.key.id)
                    if(itemValue){
                        itemValue.value = newKey.value
                        updated = true
                    }
                    break;
                }
            }
            */
            /*
            prevState.configuration.forEach(conf => {
                if (conf.key.id === newKey.key.id) {
                    conf.value = newKey.value
                    updated = true
                }
            })*/
            if (!updated) {
                prevState.configuration = [...prevState.configuration, {
                    key: selectedKey,
                    value: newValue
                }]
            }
            return {
                configuration: [...prevState.configuration],
                firmware: prevState.firmware,
                description: prevState.description,
                name: prevState.name
            }
        })
    }

    const getValue = (keyId : number) => {
        console.log(props.globalState.configuration)
        return props.globalState.configuration.find(conf => conf.key.id === keyId)?.value ?? ""
        /*
            if(){
                console.log("get value = " + conf.value)
                return conf.value
            }
        })

        for(let key in props.globalState.configuration){
            if(Number(key) === keyId){
                console.log("Set value = " + props.globalState.configuration[key])
                return props.globalState.configuration[key]
            }
        }
        
        return ""
        */
        //return props.globalState.configuration.find(keyValue => keyValue.key.id == keyId)?.value ?? ""
    }

    return (
        <Grid sx={{pt: 1, pb: 1}} container alignItems="center">
            <Grid item sm={4}>
                <Typography>{selectedKey.fullName}</Typography>
            </Grid>
            <Grid item sm={1}>
                <p>:</p>
            </Grid>
            <Grid item sm={5}>
                <Input
                    onChange={handleChange}
                    value={getValue(selectedKey.id)}
                    placeholder="valeur"/>
            </Grid>
            <Button
                size={"large"}
                sx={{
                    width: "30px", // Adjust as needed
                    height: "30px", // Adjust as needed
                    color: "error",
                }}
                color={"error"}
                onClick={() => {
                    setSelectedKeys(selectedKeys.filter(key => key.id !== selectedKey.id))
                }}
            >
                <h2>&times;</h2>
            </Button>
        </Grid>
    )
}

export default KeyValuePair;