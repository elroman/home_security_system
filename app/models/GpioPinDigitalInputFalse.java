package models;

import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinShutdown;
import com.pi4j.io.gpio.GpioProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinListener;
import com.pi4j.io.gpio.trigger.GpioTrigger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GpioPinDigitalInputFalse
    implements GpioPinDigitalInput {

    @Override
    public boolean hasDebounce(final PinState state) {
        return false;
    }

    @Override
    public int getDebounce(final PinState state) {
        return 0;
    }

    @Override
    public void setDebounce(final int debounce) {

    }

    @Override
    public void setDebounce(final int debounce, final PinState... state) {

    }

    @Override
    public boolean isHigh() {
        return false;
    }

    @Override
    public boolean isLow() {
        return false;
    }

    @Override
    public PinState getState() {
        return null;
    }

    @Override
    public boolean isState(final PinState state) {
        return false;
    }

    @Override
    public Collection<GpioTrigger> getTriggers() {
        return null;
    }

    @Override
    public void addTrigger(final GpioTrigger... trigger) {

    }

    @Override
    public void addTrigger(final List<? extends GpioTrigger> triggers) {

    }

    @Override
    public void removeTrigger(final GpioTrigger... trigger) {

    }

    @Override
    public void removeTrigger(final List<? extends GpioTrigger> triggers) {

    }

    @Override
    public void removeAllTriggers() {

    }

    @Override
    public GpioProvider getProvider() {
        return null;
    }

    @Override
    public Pin getPin() {
        return null;
    }

    @Override
    public void setName(final String name) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setTag(final Object tag) {

    }

    @Override
    public Object getTag() {
        return null;
    }

    @Override
    public void setProperty(final String key, final String value) {

    }

    @Override
    public boolean hasProperty(final String key) {
        return false;
    }

    @Override
    public String getProperty(final String key) {
        return null;
    }

    @Override
    public String getProperty(final String key, final String defaultValue) {
        return null;
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    @Override
    public void removeProperty(final String key) {

    }

    @Override
    public void clearProperties() {

    }

    @Override
    public void export(final PinMode mode) {

    }

    @Override
    public void export(final PinMode mode, final PinState defaultState) {

    }

    @Override
    public void unexport() {

    }

    @Override
    public boolean isExported() {
        return false;
    }

    @Override
    public void setMode(final PinMode mode) {

    }

    @Override
    public PinMode getMode() {
        return null;
    }

    @Override
    public boolean isMode(final PinMode mode) {
        return false;
    }

    @Override
    public void setPullResistance(final PinPullResistance resistance) {

    }

    @Override
    public PinPullResistance getPullResistance() {
        return null;
    }

    @Override
    public boolean isPullResistance(final PinPullResistance resistance) {
        return false;
    }

    @Override
    public Collection<GpioPinListener> getListeners() {
        return null;
    }

    @Override
    public void addListener(final GpioPinListener... listener) {

    }

    @Override
    public void addListener(final List<? extends GpioPinListener> listeners) {

    }

    @Override
    public boolean hasListener(final GpioPinListener... listener) {
        return false;
    }

    @Override
    public void removeListener(final GpioPinListener... listener) {

    }

    @Override
    public void removeListener(final List<? extends GpioPinListener> listeners) {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public GpioPinShutdown getShutdownOptions() {
        return null;
    }

    @Override
    public void setShutdownOptions(final GpioPinShutdown options) {

    }

    @Override
    public void setShutdownOptions(final Boolean unexport) {

    }

    @Override
    public void setShutdownOptions(final Boolean unexport, final PinState state) {

    }

    @Override
    public void setShutdownOptions(final Boolean unexport, final PinState state, final PinPullResistance resistance) {

    }

    @Override
    public void setShutdownOptions(
        final Boolean unexport, final PinState state, final PinPullResistance resistance, final PinMode mode
    ) {

    }
}
