/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.Data;
import org.dragonet.entity.metadata.EntityMetaData;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.utilities.io.PEBinaryWriter;

public class AddEntityPacket extends PEPacket {

    @Data
    public static class EntityLink {

        public long eid1;
        public long eid2;
        public byte flag;

        public void writeTo(PEBinaryWriter writer) throws IOException {
            writer.writeLong(eid1);
            writer.writeLong(eid2);
            writer.writeByte(flag);
        }
    }

    public long eid;
    public int type;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float yaw;
    public float pitch;
    public EntityMetaData meta;
    public EntityLink[] links;
    
    @Override
    public int pid() {
        return PEPacketIDs.ADD_ENTITY_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_ENTITY_SPAWNING);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeLong(eid);
            writer.writeInt(type);
            writer.writeFloat(x);
            writer.writeFloat(y);
            writer.writeFloat(z);
            writer.writeFloat(speedX);
            writer.writeFloat(speedY);
            writer.writeFloat(speedZ);
            writer.writeFloat(yaw);
            writer.writeFloat(pitch);
            writer.write(this.meta.encode());
            writer.writeShort((short) (this.links == null ? 0 : this.links.length));
            if (this.links != null) {
                for (EntityLink link : links) {
                    link.writeTo(writer);
                }
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
    }

}
