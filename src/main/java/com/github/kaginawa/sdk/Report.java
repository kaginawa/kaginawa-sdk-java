/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.kaginawa.sdk;

import javax.json.bind.annotation.JsonbProperty;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Status report from Kaginawa node that enriched by Kaginawa Server.
 * <p>
 * All fields are marked as protected (not private) for reflective access.
 * </p>
 *
 * @since 0.0.1
 */
public class Report {
    /**
     * @see Builder#newBuilder()
     */
    protected Report() {
    }

    protected String id = "";

    protected int trigger;

    protected boolean success;

    protected String runtime = "";

    @JsonbProperty("seq")
    protected int sequence;

    protected long deviceTime;

    protected long bootTime;

    @JsonbProperty("gen_ms")
    protected long genMillis;

    protected String agentVersion = "";

    protected String customId = "";

    protected String sshServerHost = "";

    protected int sshRemotePort;

    protected long sshConnectTime;

    protected String adapter = "";

    @JsonbProperty("ip4_local")
    protected String localIpV4 = "";

    @JsonbProperty("ip6_local")
    protected String localIpV6 = "";

    protected String hostname = "";

    @JsonbProperty("rtt_ms")
    protected long rttMillis;

    @JsonbProperty("upload_bps")
    protected long uploadKbps;

    @JsonbProperty("download_bps")
    protected long downloadKbps;

    protected long diskTotalBytes;

    protected long diskUsedBytes;

    protected String diskLabel = "";

    protected String diskFilesystem = "";

    protected String diskMountPoint = "";

    protected String diskDevice = "";

    protected List<UsbDevice> usbDevices = Collections.emptyList();

    protected List<String> bdLocalDevices = Collections.emptyList();

    /**
     * @since 0.1.0
     */
    protected String kernelVersion = "";

    protected List<String> errors = Collections.emptyList();

    @JsonbProperty("ip_global")
    protected String globalIp = "";

    @JsonbProperty("host_global")
    protected String globalHost = "";

    protected long serverTime;

    /**
     * Returns the device identification string, commonly hardware MAC address.
     *
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the reason of report initiation.
     * <ul>
     *     <li>-1: connected to the SSH server</li>
     *     <li>0: kaginawa started</li>
     *     <li>1 or higher: interval timer in minutes</li>
     * </ul>
     *
     * @return trigger
     */
    public int getTrigger() {
        return trigger;
    }

    /**
     * Returns the shorthand of errors.size() == 0.
     *
     * @return {@code true} if no errors reported, {@code false} otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Returns the runtime environment information such as OS name and CPU architecture.
     *
     * @return runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * Returns the number of reports generated since process start.
     *
     * @return sequence
     */
    public int getSequence() {
        return sequence;
    }

    /**
     * Returns the initiated time on the device.
     *
     * @return device time as {@link ZonedDateTime}
     */
    public ZonedDateTime getDeviceTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(deviceTime), ZoneOffset.UTC);
    }

    /**
     * Returns the initiated UTC Unix timestamp in seconds on the device.
     *
     * @return device time as unix timestamp
     */
    public long getDeviceTimeAsLong() {
        return deviceTime;
    }

    /**
     * Returns the process started time on the device.
     *
     * @return device time as {@link ZonedDateTime}
     */
    public ZonedDateTime getBootTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(bootTime), ZoneOffset.UTC);
    }

    /**
     * Returns the process started UTC Unix timestamp in seconds on the device.
     *
     * @return boot time as unix timestamp
     */
    public long getBootTimeAsLong() {
        return bootTime;
    }

    /**
     * Returns the report generation time in milliseconds.
     *
     * @return report generation time in milliseconds
     */
    public long getGenMillis() {
        return genMillis;
    }

    /**
     * Returns the Kaginawa software version.
     *
     * @return version
     */
    public String getAgentVersion() {
        return agentVersion;
    }

    /**
     * Returns the user-specified device identification string.
     *
     * @return custom ID
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * Returns the hostname of the connected SSH server.
     *
     * @return host name
     */
    public String getSshServerHost() {
        return sshServerHost;
    }

    /**
     * Returns the port number of the connected SSH server.
     *
     * @return TCP port number
     */
    public int getSshRemotePort() {
        return sshRemotePort;
    }

    /**
     * Returns the connected time to the SSH server.
     *
     * @return connect time as {@link ZonedDateTime}
     */
    public ZonedDateTime getSshConnectTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(sshConnectTime), ZoneOffset.UTC);
    }

    /**
     * Returns the connected UTC Unix timestamp in seconds to the SSH server.
     *
     * @return connect time as unix timestamp
     */
    public long getSshConnectTimeAsLong() {
        return sshConnectTime;
    }

    /**
     * Returns the name of network adapter, source of the MAC address.
     *
     * @return adapter
     */
    public String getAdapter() {
        return adapter;
    }

    /**
     * Returns the local IP v4 address.
     *
     * @return IP address
     */
    public String getLocalIpV4() {
        return localIpV4;
    }

    /**
     * Returns the local IP v6 address.
     *
     * @return IP address
     */
    public String getLocalIpV6() {
        return localIpV6;
    }

    /**
     * Returns the hostname of the device.
     *
     * @return host name
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Returns the measured round trip time in milliseconds.
     *
     * @return round trip time in milliseconds
     */
    public long getRttMillis() {
        return rttMillis;
    }

    /**
     * Returns the measured upload throughput in kbps.
     *
     * @return upload throughput in kbps
     */
    public long getUploadKbps() {
        return uploadKbps;
    }

    /**
     * Returns the measured download throughput in kbps.
     *
     * @return download throughput in kbps
     */
    public long getDownloadKbps() {
        return downloadKbps;
    }

    /**
     * Returns the total disk space in bytes.
     *
     * @return total disk space
     */
    public long getDiskTotalBytes() {
        return diskTotalBytes;
    }

    /**
     * Returns the used disk space in bytes.
     *
     * @return used disk space
     */
    public long getDiskUsedBytes() {
        return diskUsedBytes;
    }

    /**
     * Returns the disk utilization percentage.
     * <p>
     * Returns 0 if the total disk space is unknown.
     * </p>
     *
     * @return disk utilization percentage
     */
    public double getDiskUtilizationPercentage() {
        if (diskTotalBytes == 0) {
            return 0;
        }
        return (double) diskUsedBytes / (double) diskTotalBytes * 100;
    }

    /**
     * Returns the disk label.
     *
     * @return disk label
     */
    public String getDiskLabel() {
        return diskLabel;
    }

    /**
     * Returns the filesystem name of the disk.
     *
     * @return filesystem name
     */
    public String getDiskFilesystem() {
        return diskFilesystem;
    }

    /**
     * Returns the mount point of the disk (default is root).
     *
     * @return disk mount point
     */
    public String getDiskMountPoint() {
        return diskMountPoint;
    }

    /**
     * Returns the device name of the disk.
     *
     * @return disk device name
     */
    public String getDiskDevice() {
        return diskDevice;
    }

    /**
     * Returns the list of detected usb devices.
     *
     * @return list of USB devices
     */
    public List<UsbDevice> getUsbDevices() {
        return usbDevices;
    }

    /**
     * Returns the list of detected bluetooth devices.
     *
     * @return list of bluetooth devices
     */
    public List<String> getBdLocalDevices() {
        return bdLocalDevices;
    }

    /**
     * Returns the kernel version or OS version.
     * <p>
     * Supported since kaginawa 1.0.0, kaginawa-server 0.0.3 and kaginawa-sdk-java 0.1.0.
     * </p>
     *
     * @return kernel version or OS version
     * @since 0.1.0
     */
    public String getKernelVersion() {
        return kernelVersion;
    }

    /**
     * Returns the list of report generation errors.
     *
     * @return list of errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Returns the global IP address.
     *
     * @return IP address
     */
    public String getGlobalIp() {
        return globalIp;
    }

    /**
     * Returns the reverse-lookup result for global IP address.
     *
     * @return host
     */
    public String getGlobalHost() {
        return globalHost;
    }

    /**
     * Returns the server-consumed time.
     *
     * @return device time as {@link ZonedDateTime}
     */
    public ZonedDateTime getServerTime() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(serverTime), ZoneOffset.UTC);
    }

    /**
     * Returns the server-consumed UTC Unix timestamp in seconds.
     *
     * @return server-consumed time as unix timestamp
     */
    public long getServerTimeAsLong() {
        return serverTime;
    }

    /**
     * Entry of a USB device.
     */
    public static class UsbDevice {
        public UsbDevice() {
        }

        public UsbDevice(String name, String vendorId, String productId, String location) {
            this.name = name;
            this.vendorId = vendorId;
            this.productId = productId;
            this.location = location;
        }

        protected String name;

        protected String vendorId;

        protected String productId;

        protected String location;

        public String getName() {
            return name;
        }

        public String getVendorId() {
            return vendorId;
        }

        public String getProductId() {
            return productId;
        }

        public String getLocation() {
            return location;
        }
    }

    /**
     * Builds a {@link Report}.
     */
    public static class Builder {
        private final Report o = new Report();

        /**
         * @see #newBuilder()
         */
        private Builder() {
        }

        /**
         * Creates a new {@link Builder} for building a {@link Report}.
         *
         * @return a {@link Builder}
         */
        public static Builder newBuilder() {
            return new Builder();
        }

        /**
         * Sets an ID.
         *
         * @param id ID
         * @return this builder
         * @throws NullPointerException     if the id is {@code null}
         * @throws IllegalArgumentException if the id is empty
         */
        public Builder id(String id) {
            Objects.requireNonNull(id, "id is required");
            if (id.isEmpty()) {
                throw new IllegalArgumentException("id is empty");
            }
            o.id = id;
            return this;
        }

        /**
         * Sets a trigger value.
         *
         * @param trigger trigger
         * @return this builder
         */
        public Builder trigger(int trigger) {
            o.trigger = trigger;
            return this;
        }

        /**
         * Set a success state.
         *
         * @param success success
         * @return this builder
         */
        public Builder success(boolean success) {
            o.success = success;
            return this;
        }

        /**
         * Sets a runtime value.
         *
         * @param runtime runtime
         * @return this builder
         * @throws NullPointerException     if the runtime is {@code null}
         * @throws IllegalArgumentException if the runtime is empty
         */
        public Builder runtime(String runtime) {
            Objects.requireNonNull(runtime, "runtime is required");
            if (runtime.isEmpty()) {
                throw new IllegalArgumentException("runtime is empty");
            }
            o.runtime = runtime;
            return this;
        }

        /**
         * Sets a sequence number.
         *
         * @param sequence sequence
         * @return this builder
         * @throws IllegalArgumentException if the sequence is not a natural number
         */
        public Builder sequence(int sequence) {
            if (sequence < 1) {
                throw new IllegalArgumentException("sequence must be a natural number");
            }
            o.sequence = sequence;
            return this;
        }

        /**
         * Sets a device time.
         *
         * @param time device time by unix timestamp in seconds
         * @return this builder
         * @throws IllegalArgumentException if the time is negative
         */
        public Builder deviceTime(long time) {
            if (time < 0) {
                throw new IllegalArgumentException("device time must be a positive number");
            }
            o.deviceTime = time;
            return this;
        }

        /**
         * Sets a device time using {@link ZonedDateTime}.
         *
         * @param time device time
         * @return this builder
         * @throws NullPointerException if the time is {@code null}
         */
        public Builder deviceTime(ZonedDateTime time) {
            Objects.requireNonNull(time);
            o.deviceTime = time.toEpochSecond();
            return this;
        }

        /**
         * Sets a boot time.
         *
         * @param time boot time by unix timestamp in seconds
         * @return this builder
         * @throws IllegalArgumentException if the time is negative
         */
        public Builder bootTime(long time) {
            if (time < 0) {
                throw new IllegalArgumentException("device time must be a positive number");
            }
            o.bootTime = time;
            return this;
        }

        /**
         * Sets a boot time using {@link ZonedDateTime}.
         *
         * @param time boot time
         * @return this builder
         * @throws NullPointerException if the time is {@code null}
         */
        public Builder bootTime(ZonedDateTime time) {
            Objects.requireNonNull(time);
            o.bootTime = time.toEpochSecond();
            return this;
        }

        /**
         * Sets a generation time in milliseconds.
         *
         * @param genMillis generation time in milliseconds
         * @return this builder
         * @throws IllegalArgumentException if the generation time is negative
         */
        public Builder genMillis(long genMillis) {
            if (genMillis < 0) {
                throw new IllegalArgumentException("generation time must be a positive number");
            }
            o.genMillis = genMillis;
            return this;
        }

        /**
         * Sets an agent version.
         *
         * @param version version string
         * @return this builder
         * @throws NullPointerException     if the version is {@code null}
         * @throws IllegalArgumentException if the version is empty
         */
        public Builder agentVersion(String version) {
            Objects.requireNonNull(version, "version is required");
            if (version.isEmpty()) {
                throw new IllegalArgumentException("version is empty");
            }
            o.agentVersion = version;
            return this;
        }

        /**
         * Sets a custom ID.
         *
         * @param customId custom ID
         * @return this builder
         * @throws NullPointerException     if the custom ID is {@code null}
         * @throws IllegalArgumentException if the custom ID is empty
         */
        public Builder customId(String customId) {
            Objects.requireNonNull(customId, "custom ID is required");
            if (customId.isEmpty()) {
                throw new IllegalArgumentException("custom ID is empty");
            }
            o.customId = customId;
            return this;
        }

        /**
         * Sets an ssh server host.
         *
         * @param host host name
         * @return this builder
         * @throws NullPointerException     if the host is {@code null}
         * @throws IllegalArgumentException if the host is empty
         */
        public Builder sshServerHost(String host) {
            Objects.requireNonNull(host, "ssh server host is required");
            if (host.isEmpty()) {
                throw new IllegalArgumentException("ssh server host is empty");
            }
            o.sshServerHost = host;
            return this;
        }

        /**
         * Sets an ssh remote port.
         *
         * @param port TCP port number
         * @return this builder
         * @throws IllegalArgumentException if the port is out of range
         */
        public Builder sshRemotePort(int port) {
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("out of range: " + port);
            }
            o.sshRemotePort = port;
            return this;
        }

        /**
         * Sets an ssh connect time.
         *
         * @param time ssh connect time by unix timestamp in seconds
         * @return this builder
         * @throws IllegalArgumentException if the time is negative
         */
        public Builder sshConnectTime(long time) {
            if (time < 0) {
                throw new IllegalArgumentException("ssh connect time must be a positive number");
            }
            o.sshConnectTime = time;
            return this;
        }

        /**
         * Sets an ssh connect time using {@link ZonedDateTime}.
         *
         * @param time boot time
         * @return this builder
         * @throws NullPointerException if the time is {@code null}
         */
        public Builder sshConnectTime(ZonedDateTime time) {
            Objects.requireNonNull(time);
            o.sshConnectTime = time.toEpochSecond();
            return this;
        }

        /**
         * Sets an adapter name.
         *
         * @param adapter adapter
         * @return this builder
         * @throws NullPointerException     if the adapter is {@code null}
         * @throws IllegalArgumentException if the adapter is empty
         */
        public Builder adapter(String adapter) {
            Objects.requireNonNull(adapter, "adapter is required");
            if (adapter.isEmpty()) {
                throw new IllegalArgumentException("adapter is empty");
            }
            o.adapter = adapter;
            return this;
        }

        /**
         * Sets a local IPv4 address.
         *
         * @param ip IPv4 address
         * @return this builder
         * @throws NullPointerException     if the IP is {@code null}
         * @throws IllegalArgumentException if the IP is empty
         */
        public Builder localIpV4(String ip) {
            Objects.requireNonNull(ip, "local ip4 is required");
            if (ip.isEmpty()) {
                throw new IllegalArgumentException("local ip4 is empty");
            }
            o.localIpV4 = ip;
            return this;
        }

        /**
         * Sets a local IPv6 address.
         *
         * @param ip runtime
         * @return this builder
         * @throws NullPointerException     if the IP is {@code null}
         * @throws IllegalArgumentException if the IP is empty
         */
        public Builder localIpV6(String ip) {
            Objects.requireNonNull(ip, "local ip6 is required");
            if (ip.isEmpty()) {
                throw new IllegalArgumentException("local ip6 is empty");
            }
            o.localIpV6 = ip;
            return this;
        }

        /**
         * Sets a hostname.
         *
         * @param hostname runtime
         * @return this builder
         * @throws NullPointerException     if the hostname is {@code null}
         * @throws IllegalArgumentException if the hostname is empty
         */
        public Builder hostname(String hostname) {
            Objects.requireNonNull(hostname, "hostname is required");
            if (hostname.isEmpty()) {
                throw new IllegalArgumentException("hostname is empty");
            }
            o.hostname = hostname;
            return this;
        }

        /**
         * Sets a round trip time in milliseconds.
         *
         * @param rttMillis round trip time in milliseconds
         * @return this builder
         * @throws IllegalArgumentException if the round trip time is negative
         */
        public Builder rttMillis(long rttMillis) {
            if (rttMillis < 0) {
                throw new IllegalArgumentException("round trip time must be a positive number");
            }
            o.rttMillis = rttMillis;
            return this;
        }

        /**
         * Sets a upload throughput in kbps (kilo bit per second).
         *
         * @param uploadKbps upload throughput in kbps
         * @return this builder
         * @throws IllegalArgumentException if the upload throughput is negative
         */
        public Builder uploadKbps(long uploadKbps) {
            if (uploadKbps < 0) {
                throw new IllegalArgumentException("upload throughput be a positive number");
            }
            o.uploadKbps = uploadKbps;
            return this;
        }

        /**
         * Sets a download throughput in kbps (kilo bit per second).
         *
         * @param downloadKbps download throughput in kbps
         * @return this builder
         * @throws IllegalArgumentException if the download throughput is negative
         */
        public Builder downloadKbps(long downloadKbps) {
            if (downloadKbps < 0) {
                throw new IllegalArgumentException("download throughput be a positive number");
            }
            o.downloadKbps = downloadKbps;
            return this;
        }

        /**
         * Sets a total disk space in bytes.
         *
         * @param bytes total disk space in bytes
         * @return this builder
         * @throws IllegalArgumentException if the total disk space is negative
         */
        public Builder diskTotalBytes(long bytes) {
            if (bytes < 0) {
                throw new IllegalArgumentException("total disk space must be a positive number");
            }
            o.diskTotalBytes = bytes;
            return this;
        }

        /**
         * Sets a used disk space in bytes.
         *
         * @param bytes used disk space in bytes
         * @return this builder
         * @throws IllegalArgumentException if the used disk space is negative
         */
        public Builder diskUsedBytes(long bytes) {
            if (bytes < 0) {
                throw new IllegalArgumentException("used disk space must be a positive number");
            }
            o.diskUsedBytes = bytes;
            return this;
        }

        /**
         * Sets a disk label.
         *
         * @param label disk label
         * @return this builder
         * @throws NullPointerException     if the label is {@code null}
         * @throws IllegalArgumentException if the label is empty
         */
        public Builder diskLabel(String label) {
            Objects.requireNonNull(label, "disk label is required");
            if (label.isEmpty()) {
                throw new IllegalArgumentException("disk label is empty");
            }
            o.diskLabel = label;
            return this;
        }

        /**
         * Sets a disk filesystem name.
         *
         * @param fs disk filesystem name
         * @return this builder
         * @throws NullPointerException     if the fs is {@code null}
         * @throws IllegalArgumentException if the fs is empty
         */
        public Builder diskFilesystem(String fs) {
            Objects.requireNonNull(fs, "disk filesystem name is required");
            if (fs.isEmpty()) {
                throw new IllegalArgumentException("disk filesystem name is empty");
            }
            o.diskFilesystem = fs;
            return this;
        }

        /**
         * Sets a disk mount point.
         *
         * @param mountPoint disk mount point
         * @return this builder
         * @throws NullPointerException     if the disk mount point is {@code null}
         * @throws IllegalArgumentException if the disk mount point is empty
         */
        public Builder diskMountPoint(String mountPoint) {
            Objects.requireNonNull(mountPoint, "disk mount point is required");
            if (mountPoint.isEmpty()) {
                throw new IllegalArgumentException("disk mount point is empty");
            }
            o.diskMountPoint = mountPoint;
            return this;
        }

        /**
         * Sets a disk device name.
         *
         * @param device disk device name
         * @return this builder
         * @throws NullPointerException     if the device is {@code null}
         * @throws IllegalArgumentException if the device is empty
         */
        public Builder diskDevice(String device) {
            Objects.requireNonNull(device, "disk device is required");
            if (device.isEmpty()) {
                throw new IllegalArgumentException("disk device is empty");
            }
            o.diskDevice = device;
            return this;
        }

        /**
         * Sets a list of USB devices.
         *
         * @param devices list of USB devices
         * @return this builder
         * @throws NullPointerException if the devices is {@code null}
         */
        public Builder usbDevices(List<UsbDevice> devices) {
            Objects.requireNonNull(devices);
            o.usbDevices = devices;
            return this;
        }

        /**
         * Sets a list of Bluetooth local devices.
         *
         * @param devices list of Bluetooth local devices
         * @return this builder
         * @throws NullPointerException if the devices is {@code null}
         */
        public Builder bdLocalDevices(List<String> devices) {
            Objects.requireNonNull(devices);
            o.bdLocalDevices = devices;
            return this;
        }

        /**
         * Sets a kernel version or OS version.
         *
         * @param version kernel version or OS version
         * @throws NullPointerException     if the version is {@code null}
         * @throws IllegalArgumentException if the version is empty
         * @since 0.1.0
         */
        public Builder kernelVersion(String version) {
            Objects.requireNonNull(version);
            if (version.isEmpty()) {
                throw new IllegalArgumentException("kernel version is empty");
            }
            o.kernelVersion = version;
            return this;
        }

        /**
         * Sets a list of error messages.
         *
         * @param errors list of error messages
         * @return this builder
         * @throws NullPointerException if the errors is {@code null}
         */
        public Builder errors(List<String> errors) {
            Objects.requireNonNull(errors);
            o.errors = errors;
            return this;
        }

        /**
         * Sets a global IPv4 address.
         *
         * @param ip IPv4 address
         * @return this builder
         * @throws NullPointerException     if the IP is {@code null}
         * @throws IllegalArgumentException if the IP is empty
         */
        public Builder globalIp(String ip) {
            Objects.requireNonNull(ip, "global ip is required");
            if (ip.isEmpty()) {
                throw new IllegalArgumentException("global ip is empty");
            }
            o.globalIp = ip;
            return this;
        }

        /**
         * Sets a local host name.
         *
         * @param host host name
         * @return this builder
         * @throws NullPointerException     if the host is {@code null}
         * @throws IllegalArgumentException if the host is empty
         */
        public Builder globalHost(String host) {
            Objects.requireNonNull(host, "host is required");
            if (host.isEmpty()) {
                throw new IllegalArgumentException("host is empty");
            }
            o.globalHost = host;
            return this;
        }

        /**
         * Sets a server consumed time.
         *
         * @param time server consumed time by unix timestamp in seconds
         * @return this builder
         * @throws IllegalArgumentException if the time is negative
         */
        public Builder serverTime(long time) {
            if (time < 0) {
                throw new IllegalArgumentException("server consumed time must be a positive number");
            }
            o.serverTime = time;
            return this;
        }

        /**
         * Sets a server consumed time using {@link ZonedDateTime}.
         *
         * @param time server consumed time
         * @return this builder
         * @throws NullPointerException if the time is {@code null}
         */
        public Builder serverTime(ZonedDateTime time) {
            Objects.requireNonNull(time);
            o.serverTime = time.toEpochSecond();
            return this;
        }

        /**
         * Returns a new {@link Report} built from the current state of this builder.
         *
         * @return a new {@link Report}
         * @throws IllegalStateException if the required parameter(s) are not set
         */
        public Report build() {
            if (o.id == null || o.id.isEmpty()) {
                throw new IllegalStateException("id is not set yet");
            }
            return o;
        }
    }
}
