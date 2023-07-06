import { HttpService } from '@nestjs/axios';
import { JwtCredentialPayload } from 'did-jwt-vc';
import { IssuerType } from 'did-jwt-vc/lib/types';
import { AxiosResponse } from '@nestjs/terminus/dist/health-indicator/http/axios.interfaces';
import { DIDDocument } from 'did-resolver';
import {
  Injectable,
  InternalServerErrorException,
  Logger,
} from '@nestjs/common';

@Injectable()
export class IdentityUtilsService {
  constructor(private readonly httpService: HttpService) {}

  async signVC(
    credentialPlayload: JwtCredentialPayload,
    did: IssuerType
  ): Promise<String> {
    try {
      const signedVCResponse: AxiosResponse =
        await this.httpService.axiosRef.post(
          `${process.env.IDENTITY_BASE_URL}/utils/sign`,
          {
            DID: did,
            payload: JSON.stringify(credentialPlayload),
          }
        );
      return signedVCResponse.data.signed as string;
    } catch (err) {
      Logger.error('Error signing VC: ', err);
      throw new InternalServerErrorException('Error signing VC');
    }
  }

  async resolveDID(issuer): Promise<DIDDocument> {
    try {
      const verificationURL = `${process.env.IDENTITY_BASE_URL}/did/resolve/${issuer}`;
      const dIDResponse: AxiosResponse = await this.httpService.axiosRef.get(
        verificationURL
      );

      return dIDResponse.data as DIDDocument;
    } catch (err) {
      Logger.error('Error resolving DID: ', err);
      throw new InternalServerErrorException('Error resolving DID');
    }
  }

  async generateDID(
    alsoKnownAs: ReadonlyArray<String>
  ): Promise<ReadonlyArray<DIDDocument>> {
    try {
      const didGenRes: AxiosResponse = await this.httpService.axiosRef.post(
        `${process.env.IDENTITY_BASE_URL}/did/generate`,
        {
          content: [
            {
              alsoKnownAs: alsoKnownAs,
              services: [
                {
                  id: 'IdentityHub',
                  type: 'IdentityHub',
                  serviceEndpoint: {
                    '@context': 'schema.identity.foundation/hub',
                    '@type': 'UserServiceEndpoint',
                    instance: ['did:test:hub.id'],
                  },
                },
              ],
            },
          ],
        }
      );
      return didGenRes.data as ReadonlyArray<DIDDocument>;
    } catch (err) {
      Logger.error('Error generating DID: ', err);
      throw new InternalServerErrorException('Error generating DID');
    }
  }
}
