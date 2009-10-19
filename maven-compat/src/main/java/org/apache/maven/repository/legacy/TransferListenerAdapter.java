package org.apache.maven.repository.legacy;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.repository.ArtifactTransferEvent;
import org.apache.maven.repository.ArtifactTransferListener;
import org.apache.maven.repository.MavenArtifact;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener;
import org.apache.maven.wagon.resource.Resource;

public class TransferListenerAdapter
    implements TransferListener
{

    private ArtifactTransferListener listener;

    public static TransferListener newAdapter( ArtifactTransferListener listener )
    {
        if ( listener == null )
        {
            return null;
        }
        else
        {
            return new TransferListenerAdapter( listener );
        }
    }

    private TransferListenerAdapter( ArtifactTransferListener listener )
    {
        this.listener = listener;
    }

    public void debug( String message )
    {
    }

    public void transferCompleted( TransferEvent transferEvent )
    {
        listener.transferCompleted( wrap( transferEvent ) );
    }

    public void transferError( TransferEvent transferEvent )
    {
    }

    public void transferInitiated( TransferEvent transferEvent )
    {
        listener.transferInitiated( wrap( transferEvent ) );
    }

    public void transferProgress( TransferEvent transferEvent, byte[] buffer, int length )
    {
        listener.transferProgress( wrap( transferEvent ), buffer, length );
    }

    public void transferStarted( TransferEvent transferEvent )
    {
    }

    private ArtifactTransferEvent wrap( TransferEvent event )
    {
        if ( event == null )
        {
            return null;
        }
        else
        {
            String wagon = event.getWagon().getRepository().getUrl();

            MavenArtifact artifact = wrap( event.getResource() );

            ArtifactTransferEvent evt;
            if ( event.getException() != null )
            {
                evt = new ArtifactTransferEvent( wagon, event.getException(), event.getRequestType(), artifact );
            }
            else
            {
                evt = new ArtifactTransferEvent( wagon, event.getEventType(), event.getRequestType(), artifact );
            }

            evt.setLocalFile( event.getLocalFile() );

            return evt;
        }
    }

    private MavenArtifact wrap( Resource resource )
    {
        if ( resource == null )
        {
            return null;
        }
        else
        {
            return new MavenArtifact( resource.getName(), resource.getContentLength() );
        }
    }

}
