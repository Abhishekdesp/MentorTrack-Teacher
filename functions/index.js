const functions = require('firebase-functions');
const admin = require('firebase-admin');
const fetch = require('node-fetch');

admin.initializeApp();

const FIREFLIES_API_KEY = functions.config().fireflies.apikey;

exports.getFirefliesMeetingSummary = functions.https.onCall(async (data, context) => {
  try {
    const { meetingId } = data;

    if (!meetingId) {
      throw new functions.https.HttpsError('invalid-argument', 'Meeting ID is required');
    }

    const graphqlQuery = {
      query: `
        query GetTranscript($id: String!) {
          transcript(id: $id) {
            title
            date
            action_items {
              text
              assignee
            }
            tasks {
              text
            }
            topics {
              text
            }
          }
        }
      `,
      variables: { id: meetingId }
    };

    const response = await fetch('https://api.fireflies.ai/graphql', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${FIREFLIES_API_KEY}`
      },
      body: JSON.stringify(graphqlQuery)
    });

    const result = await response.json();

    if (result.errors) {
      console.error('Fireflies API Error:', result.errors);
      throw new functions.https.HttpsError('internal', result.errors[0].message);
    }

    const simplifiedData = {
      meetingTitle: result.data.transcript?.title || 'Untitled Meeting',
      meetingDate: result.data.transcript?.date || '',
      actionItems: result.data.transcript?.action_items || [],
      keyTasks: result.data.transcript?.tasks || [],
      topics: result.data.transcript?.topics || []
    };

    return simplifiedData;

  } catch (error) {
    console.error('Error:', error);
    throw new functions.https.HttpsError('internal', 'Failed to fetch meeting summary: ' + error.message);
  }
});