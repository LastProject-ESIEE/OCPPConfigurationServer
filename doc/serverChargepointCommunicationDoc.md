# How does the chargepoints communicate with the server ?

## Overview

This document is about the process and the description of the server to chargepoints and chargepoints to server.
The processes will be explained with examples and according to the OCPP protocol (1.6).

This application is ready to accept other OCPP version such as 2.0.1.

## Warning

> 1 : If a field is required for our application, it will be marked like that [X].

> 2 : The communication between the two machines is a request-response, meaning, for every request sent a response 
> is expected.

> 3 : The OCPP 2.0.1 protocol is not yet implemented, see [Not implemented](#ocpp-201) for more explanation.

## Processes

### First boot

On startup, the charge point sends a **BootNotificationRequest** to the server containing these information :
- The charge point's constructor         [X] ;
- The charge point's model                 [X] ;
- The charge point's serial number     [X] ;
- The charge box serial number               ;
- The charge point's firmware version [X].

After receiving this message, we search in the database a chargepoint with the same serial number and vendor.
Then, the server responds with a **BootNotificationResponse** to the charge point containing these information :
- The status (it can be Accepted (We found a corresponding charge point in the database)
Rejected (we did not find a corresponding charge point)) [X] ;
- The current time [X] ;
- The interval which the charge point should respect before sending a message [X].

Then, the server would check the current state of the charge point before doing anything.

### Firmware process

If the chargepoint is in FIRMWARE mode in the database, the server will compare the firmware target 
in the linked configuration of the chargepoint to the current chargepoint's firmware.

If the target firmware is the same as the current firmware, 
we just skip this process and put the charge point into CONFIGURATION mode.

If the target firmware is not the same, we downgrade or upgrade the firmware, step by step.

During the firmware process, the server will choose intermediary firmwares before installing the target firmware,
so the process is technically a loop until the target firmware has been installed.

The process is started by the server sending an **UpdateFirmwareRequest** containing :
- The URL (FTP or HTTP/S) for the firmware file (.fwi) [X] ;
- The retrieve date when the charge point should download the firmware [X].

The charge point should respond with an **UpdateFirmwareResponse** which is an empty message.
It is like an ACK packet in a IP/TCP communication.

The charge point will sometimes send a **FirmwareStatusNotificationRequest** containing the status of the download.
The status can be :
- Downloaded, the firmware has been downloaded ;
- DownloadFailed, the firmware couldn't be downloaded ;
- Downloading, the charge point currently downloads the firmware ;
- Idle, the charge point is waiting for a process to finish before installing the firmware ;
- InstallationFailed, the firmware couldn't be installed on the charge point ;
- Installing, the charge point currently installs the firmware ;
- Installed, the firmware has been installed on the charge point.

If the status is DownloadFailed/InstallationFailed, all the processes are stopped.

If the status is Installed, we continue by rebooting the machine and continue by switching the charge point into
the CONFIGURATION mode.

If the status is something else, we just wait for the confirmation of the installation of the firmware.

### Configuration process

If the charge point is in CONFIGURATION mode in the database, the server will compare the last update date in 
the linked configuration and the last update date in the chargepoint table.

If the configuration date is more recent than the charge point date, we skip this process.

Else, we search for the key/value in the configuration, and we send n packets for n key/value.

The server sends a **ChangeConfigurationRequest** containing :
- The key [X] ;
- The value [X] ;

The charge point will respond with a **ChangeConfigurationResponse** containing a status.
This status can be :
- Accepted, the change has been accepted ;
- Rejected, the change has been rejected because the value is wrong ;
- RebootRequired, the change has been accepted but the charge point needs to reboot ;
- NotSupported, the given key is not correct.

If all the messages sent by the server are Accepted or RebootRequired, the configuration is finished,
and we send the last order containing the new server address, and we reboot the charge point.

If not, we stop the process.

## Other

### Bugs

### Not implemented

#### OCPP 2.0.1

The OCPP 2.0.1 protocol is not implemented because this application has been tested with Alfen BV's charge points
(Eve Single S-Line). 

We could not find the correct *Component* object for the **SetVariablesRequest**, except a Component called "none"
for this charge point.

However, the component "none" does not seem to work when giving new value to a key, it will always respond by
a Rejected.

Moreover, here is a list of all the known Component for Alfen BV (for firmware >= 6.1).

> Note : When marked by a [X], it means the *Component* is an OCPP standard, see the 
> [official documentation](https://openchargealliance.org/protocols/open-charge-point-protocol/#OCPP2.0.1).

- AlignedDataCtrlr [X] ;
- AuthCtrlr [X] ;
- AuthCacheCtrlr [X] ;
- ChargingStation [X] ;
- ClockCtrlr [X] ;
- DeviceDataCtrlr [X] ;
- DisplayMessageCtrlr [X] ;
- LocalAuthListCtrlr [X] ;
- MonitoringCtrlr [X] ;
- none ;
- OCPPCommCtrlr [X] ;
- ReservationCtrlr [X] ;
- SampledDataCtrlr [X] ;
- SecurityCtrlr [X] ;
- SmartChargingCtrlr [X] ;
- TariffCostCtrlr [X] ;
- TxCtrlr [X].



