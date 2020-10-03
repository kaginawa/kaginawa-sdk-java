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

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReportTests {
    @Test
    public void testBuilder() {
        var builder = Report.Builder.newBuilder();
        assertNotNull(builder.id("b8:27:eb:73:90:9f"));
        assertNotNull(builder.trigger(3));
        assertNotNull(builder.success(true));
        assertNotNull(builder.runtime("linux arm"));
        assertNotNull(builder.sequence(123));
        assertNotNull(builder.deviceTime(1591270237L));
        assertNotNull(builder.bootTime(1589472032L));
        assertNotNull(builder.genMillis(42L));
        assertNotNull(builder.agentVersion("v0.0.7"));
        assertNotNull(builder.customId("test-rpi"));
        assertNotNull(builder.sshServerHost("example.com"));
        assertNotNull(builder.sshRemotePort(35771));
        assertNotNull(builder.sshConnectTime(1591240218L));
        assertNotNull(builder.adapter("eth0"));
        assertNotNull(builder.localIpV4("10.128.0.10"));
        assertNotNull(builder.localIpV6("fe80::c57b:eef0:7e85:4f35"));
        assertNotNull(builder.hostname("test-rpi.local"));
        assertNotNull(builder.rttMillis(94L));
        assertNotNull(builder.uploadKbps(3868L));
        assertNotNull(builder.downloadKbps(3430L));
        assertNotNull(builder.diskTotalBytes(2691837952L));
        assertNotNull(builder.diskUsedBytes(2358947840L));
        assertNotNull(builder.kernelVersion("19.6.0"));
        assertNotNull(builder.diskLabel("OS"));
        assertNotNull(builder.diskFilesystem("ext4"));
        assertNotNull(builder.diskMountPoint("/"));
        assertNotNull(builder.diskDevice("/dev/root"));
        assertNotNull(builder.usbDevices(List.of(
                new Report.UsbDevice("Arduino SA Uno R3 (CDC ACM)", "2341", "0043", "Bus 001 Device 004"),
                new Report.UsbDevice("Linux Foundation 2.0 root hub", "1d6b", "0002", "Bus 001 Device 001")
        )));
        assertNotNull(builder.bdLocalDevices(List.of("B8:27:EB:D9:3A:35")));
        assertNotNull(builder.errors(List.of("error entry test")));
        assertNotNull(builder.globalIp("202.222.12.138"));
        assertNotNull(builder.globalHost("ngn-nat1.v4.open.ad.jp"));
        assertNotNull(builder.serverTime(1591270237L));

        var report = builder.build();
        assertNotNull(report);
        assertEquals("b8:27:eb:73:90:9f", report.getId());
        assertEquals(3, report.getTrigger());
        assertTrue(report.isSuccess());
        assertEquals("linux arm", report.getRuntime());
        assertEquals(123, report.getSequence());
        assertEquals(1591270237L, report.getDeviceTimeAsLong());
        assertEquals(1589472032L, report.getBootTimeAsLong());
        assertEquals(42L, report.getGenMillis());
        assertEquals("v0.0.7", report.getAgentVersion());
        assertEquals("test-rpi", report.getCustomId());
        assertEquals("example.com", report.getSshServerHost());
        assertEquals(35771, report.getSshRemotePort());
        assertEquals(1591240218L, report.getSshConnectTimeAsLong());
        assertEquals("eth0", report.getAdapter());
        assertEquals("10.128.0.10", report.getLocalIpV4());
        assertEquals("fe80::c57b:eef0:7e85:4f35", report.getLocalIpV6());
        assertEquals("test-rpi.local", report.getHostname());
        assertEquals(94L, report.getRttMillis());
        assertEquals(3868L, report.getUploadKbps());
        assertEquals(3430L, report.getDownloadKbps());
        assertEquals(2691837952L, report.getDiskTotalBytes());
        assertEquals(2358947840L, report.getDiskUsedBytes());
        assertEquals("OS", report.getDiskLabel());
        assertEquals("ext4", report.getDiskFilesystem());
        assertEquals("/", report.getDiskMountPoint());
        assertEquals("/dev/root", report.getDiskDevice());
        assertEquals(2, report.getUsbDevices().size());
        assertEquals("Arduino SA Uno R3 (CDC ACM)", report.getUsbDevices().get(0).getName());
        assertEquals("2341", report.getUsbDevices().get(0).getVendorId());
        assertEquals("0043", report.getUsbDevices().get(0).getProductId());
        assertEquals("Bus 001 Device 004", report.getUsbDevices().get(0).getLocation());
        assertEquals("Linux Foundation 2.0 root hub", report.getUsbDevices().get(1).getName());
        assertEquals("1d6b", report.getUsbDevices().get(1).getVendorId());
        assertEquals("0002", report.getUsbDevices().get(1).getProductId());
        assertEquals("Bus 001 Device 001", report.getUsbDevices().get(1).getLocation());
        assertEquals(1, report.getBdLocalDevices().size());
        assertEquals("B8:27:EB:D9:3A:35", report.getBdLocalDevices().get(0));
        assertEquals("19.6.0", report.getKernelVersion());
        assertEquals(1, report.getErrors().size());
        assertEquals("error entry test", report.getErrors().get(0));
        assertEquals("202.222.12.138", report.getGlobalIp());
        assertEquals("ngn-nat1.v4.open.ad.jp", report.getGlobalHost());
        assertEquals(1591270237L, report.getServerTimeAsLong());
    }

    @Test
    public void testBuilder_ISE() {
        var builder = Report.Builder.newBuilder();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    public void testBuilder_NPE() {
        var builder = Report.Builder.newBuilder();
        assertThrows(NullPointerException.class, () -> builder.id(null));
        assertThrows(NullPointerException.class, () -> builder.runtime(null));
        assertThrows(NullPointerException.class, () -> builder.agentVersion(null));
        assertThrows(NullPointerException.class, () -> builder.customId(null));
        assertThrows(NullPointerException.class, () -> builder.sshServerHost(null));
        assertThrows(NullPointerException.class, () -> builder.adapter(null));
        assertThrows(NullPointerException.class, () -> builder.localIpV4(null));
        assertThrows(NullPointerException.class, () -> builder.localIpV6(null));
        assertThrows(NullPointerException.class, () -> builder.hostname(null));
        assertThrows(NullPointerException.class, () -> builder.diskLabel(null));
        assertThrows(NullPointerException.class, () -> builder.diskFilesystem(null));
        assertThrows(NullPointerException.class, () -> builder.diskMountPoint(null));
        assertThrows(NullPointerException.class, () -> builder.diskDevice(null));
        assertThrows(NullPointerException.class, () -> builder.usbDevices(null));
        assertThrows(NullPointerException.class, () -> builder.bdLocalDevices(null));
        assertThrows(NullPointerException.class, () -> builder.kernelVersion(null));
        assertThrows(NullPointerException.class, () -> builder.errors(null));
        assertThrows(NullPointerException.class, () -> builder.globalIp(null));
        assertThrows(NullPointerException.class, () -> builder.globalHost(null));
    }

    @Test
    public void testBuilder_IAE() {
        var builder = Report.Builder.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.id(""));
        assertThrows(IllegalArgumentException.class, () -> builder.runtime(""));
        assertThrows(IllegalArgumentException.class, () -> builder.sequence(0));
        assertThrows(IllegalArgumentException.class, () -> builder.sequence(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.deviceTime(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.bootTime(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.genMillis(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.agentVersion(""));
        assertThrows(IllegalArgumentException.class, () -> builder.customId(""));
        assertThrows(IllegalArgumentException.class, () -> builder.sshServerHost(""));
        assertThrows(IllegalArgumentException.class, () -> builder.sshRemotePort(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.sshConnectTime(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.adapter(""));
        assertThrows(IllegalArgumentException.class, () -> builder.localIpV4(""));
        assertThrows(IllegalArgumentException.class, () -> builder.localIpV6(""));
        assertThrows(IllegalArgumentException.class, () -> builder.hostname(""));
        assertThrows(IllegalArgumentException.class, () -> builder.rttMillis(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.uploadKbps(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.downloadKbps(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.diskTotalBytes(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.diskUsedBytes(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.diskLabel(""));
        assertThrows(IllegalArgumentException.class, () -> builder.diskFilesystem(""));
        assertThrows(IllegalArgumentException.class, () -> builder.diskMountPoint(""));
        assertThrows(IllegalArgumentException.class, () -> builder.diskDevice(""));
        assertThrows(IllegalArgumentException.class, () -> builder.serverTime(-1));
        assertThrows(IllegalArgumentException.class, () -> builder.globalIp(""));
        assertThrows(IllegalArgumentException.class, () -> builder.globalHost(""));
        assertThrows(IllegalArgumentException.class, () -> builder.kernelVersion(""));
    }

    @Test
    public void testBuilder_ZDT() {
        var id = "01:02:03:04:05:06";
        var zdt = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC.normalized());
        assertEquals(zdt, Report.Builder.newBuilder().id(id).deviceTime(zdt).build().getDeviceTime());
        assertEquals(zdt, Report.Builder.newBuilder().id(id).bootTime(zdt).build().getBootTime());
        assertEquals(zdt, Report.Builder.newBuilder().id(id).sshConnectTime(zdt).build().getSshConnectTime());
        assertEquals(zdt, Report.Builder.newBuilder().id(id).serverTime(zdt).build().getServerTime());
    }

    @Test
    public void testGetDiskUtilizationPercentage() {
        var acceptDelta = 0.00001;
        var tests = List.of(
                // 0: total, 1: used, 3: expected percentage
                List.of(2691837952L, 2358947840L, 87.6333524552),
                List.of(2768658432L, 2296553472L, 82.9482411213),
                List.of(0, 123L, 0),
                List.of(111L, 0, 0),
                List.of(0, 0, 0));
        tests.forEach(t -> {
            var report = Report.Builder.newBuilder().id("01:02:03:04:05:06")
                    .diskTotalBytes(t.get(0).longValue())
                    .diskUsedBytes(t.get(1).longValue())
                    .build();
            assertEquals(t.get(2).doubleValue(), report.getDiskUtilizationPercentage(), acceptDelta);
        });
    }
}
